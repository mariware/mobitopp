package edu.kit.ifv.mobitopp.data.local;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Map;
import java.util.function.Supplier;

import edu.kit.ifv.mobitopp.data.DataRepositoryForPopulationSynthesis;
import edu.kit.ifv.mobitopp.data.DataRepositoryForSimulation;
import edu.kit.ifv.mobitopp.data.DataSource;
import edu.kit.ifv.mobitopp.data.DemandZoneRepository;
import edu.kit.ifv.mobitopp.data.InputSpecification;
import edu.kit.ifv.mobitopp.data.Network;
import edu.kit.ifv.mobitopp.data.PanelDataRepository;
import edu.kit.ifv.mobitopp.data.PersonLoader;
import edu.kit.ifv.mobitopp.data.StartDateSpecification;
import edu.kit.ifv.mobitopp.data.Zone;
import edu.kit.ifv.mobitopp.data.ZoneRepository;
import edu.kit.ifv.mobitopp.data.local.configuration.MatrixConfiguration;
import edu.kit.ifv.mobitopp.data.local.serialiser.ZoneRepositorySerialiser;
import edu.kit.ifv.mobitopp.dataimport.ChargingDataFactory;
import edu.kit.ifv.mobitopp.dataimport.DefaultPower;
import edu.kit.ifv.mobitopp.dataimport.StructuralData;
import edu.kit.ifv.mobitopp.network.SimpleRoadNetwork;
import edu.kit.ifv.mobitopp.populationsynthesis.Population;
import edu.kit.ifv.mobitopp.populationsynthesis.serialiser.DemandDataDeserialiser;
import edu.kit.ifv.mobitopp.populationsynthesis.serialiser.DemandDataFolder;
import edu.kit.ifv.mobitopp.result.ResultWriter;
import edu.kit.ifv.mobitopp.simulation.CarSharingWriter;
import edu.kit.ifv.mobitopp.simulation.ChargingListener;
import edu.kit.ifv.mobitopp.simulation.ElectricChargingWriter;
import edu.kit.ifv.mobitopp.simulation.ImpedanceCarSharing;
import edu.kit.ifv.mobitopp.simulation.ImpedanceIfc;
import edu.kit.ifv.mobitopp.simulation.LocalPersonLoader;
import edu.kit.ifv.mobitopp.simulation.PublicTransportData;
import edu.kit.ifv.mobitopp.simulation.VehicleBehaviour;
import edu.kit.ifv.mobitopp.visum.VisumNetwork;

public class LocalFiles implements DataSource {

	private static final String defaultZoneRepository = "output/zone-repository/";
	
	private File zoneRepositoryFolder;
	private File matrixConfigurationFile;
	private File demandDataFolder;
	private File attractivityDataFile;
	private ChargingType charging;
	private String defaultChargingPower;

	public LocalFiles() {
		charging = ChargingType.unlimited;
		zoneRepositoryFolder = new File(defaultZoneRepository);
	}
	
	public String getZoneRepositoryFolder() {
		return Convert.asString(zoneRepositoryFolder);
	}
	
	public void setZoneRepositoryFolder(String zoneRepositoryFile) {
		this.zoneRepositoryFolder = Convert.asFile(zoneRepositoryFile);
	}

	public String getMatrixConfigurationFile() {
		return Convert.asString(matrixConfigurationFile);
	}

	public void setMatrixConfigurationFile(String matrixConfigurationFile) {
		this.matrixConfigurationFile = Convert.asFile(matrixConfigurationFile);
	}

	public String getDemandDataFolder() {
		return Convert.asString(demandDataFolder);
	}

	public void setDemandDataFolder(String demandDataFolder) {
		this.demandDataFolder = Convert.asFile(demandDataFolder);
	}

	public String getAttractivityDataFile() {
		return Convert.asString(attractivityDataFile);
	}
	
	public void setAttractivityDataFile(String attractivityDataFile) {
		this.attractivityDataFile = Convert.asFile(attractivityDataFile);
	}

	public ChargingType getCharging() {
		return charging;
	}

	public void setCharging(ChargingType charging) {
		this.charging = charging;
	}

	public String getDefaultChargingPower() {
		return defaultChargingPower;
	}

	public void setDefaultChargingPower(String defaultChargingPower) {
		this.defaultChargingPower = defaultChargingPower;
	}

	@Override
	public DataRepositoryForPopulationSynthesis forPopulationSynthesis(
			VisumNetwork visumNetwork, SimpleRoadNetwork roadNetwork, StructuralData demographyData,
			PanelDataRepository panelDataRepository, int numberOfZones, StartDateSpecification input,
			ResultWriter results) throws IOException {
		Matrices matrices = matrices();
		ChargingListener electricChargingWriter = new ElectricChargingWriter(results);
		ZoneRepository zoneRepository = loadZonesFromVisum(visumNetwork, roadNetwork);
		initialiseResultWriting(zoneRepository, results, electricChargingWriter);
		DemandZoneRepository demandZoneRepository = demandZoneRepository(zoneRepository, demographyData);
		ImpedanceIfc impedance = impedance(input, matrices, zoneRepository);
		DemandDataFolder demandData = demandDataFolder(zoneRepository, numberOfZones);
		return new LocalDataForPopulationSynthesis(matrices, demandZoneRepository, panelDataRepository,
				impedance, demandData, results);
	}

	private DemandZoneRepository demandZoneRepository(
			ZoneRepository zoneRepository, StructuralData demographyData) {
		return LocalDemandZoneRepository.from(zoneRepository, demographyData);
	}

	private DemandDataFolder demandDataFolder(ZoneRepository zoneRepository, int numberOfZones)
			throws IOException {
		Map<Integer, Zone> mapping = new LocalZoneLoader(zoneRepository).mapZones(numberOfZones);
		ZoneRepository zonesToSimulate = new LocalZoneRepository(mapping);
		return DemandDataFolder.at(this.demandDataFolder, zoneRepository, zonesToSimulate);
	}

	private Matrices matrices() throws FileNotFoundException {
		MatrixConfiguration matrixConfiguration = matrixConfiguration();
		return new MatrixRepository(matrixConfiguration);
	}

	private MatrixConfiguration matrixConfiguration() throws FileNotFoundException {
		File configFile = matrixConfigurationFile;
		File matrixFolder = configFile.getParentFile();
		return MatrixConfiguration.from(configFile, matrixFolder);
	}

	private ZoneRepository loadZonesFromVisum(
			VisumNetwork visumNetwork, SimpleRoadNetwork roadNetwork) {
		ZoneRepository fromVisum = LocalZoneRepository
				.from(visumNetwork, roadNetwork, charging, defaultPower(), attractivityDataFile);
		ZoneRepositorySerialiser serialised = createSerialiser();
		serialised.serialise(fromVisum);
		return fromVisum;
	}

	private void initialiseResultWriting(
			ZoneRepository zoneRepository, ResultWriter results,
			ChargingListener electricChargingWriter) {
		CarSharingWriter carSharingWriter = new CarSharingWriter(results);
		for (Zone zone : zoneRepository.getZones()) {
			zone.charging().register(electricChargingWriter);
			zone.carSharing().register(carSharingWriter);
		}
	}

	private DefaultPower defaultPower() {
		if (Convert.asFile(defaultChargingPower).exists()) {
			return new DefaultPower(defaultChargingPower);
		}
		System.out.println("Default charging power file not specified using zero charging power.");
		return DefaultPower.zero;
	}

	private void addOpportunities(ZoneRepository zoneRepository, int numberOfZones) throws IOException {
		DemandDataFolder demandData = demandDataFolder(zoneRepository, numberOfZones);
		try (DemandDataDeserialiser deserialiser = demandData.deserialiseFromCsv()) {
			deserialiser.addOpportunitiesTo(zoneRepository);
		} catch (Exception e) {
			throw new IOException("Could not load demand data.", e);
		}
	}

	private ImpedanceIfc impedance(
			StartDateSpecification input, Matrices matrices, ZoneRepository zoneRepository) {
		ImpedanceLocalData localImpedance = new ImpedanceLocalData(matrices, zoneRepository,
				input.startDate());
		return new ImpedanceCarSharing(localImpedance);
	}

	@Override
	public DataRepositoryForSimulation forSimulation(
			Supplier<Network> network, int numberOfZones, InputSpecification input,
			PublicTransportData data, ResultWriter results, ElectricChargingWriter electricChargingWriter)
			throws IOException {
		Matrices matrices = matrices();
		ZoneRepository zoneRepository = loadZonesFromMobiTopp(network);
		initialiseResultWriting(zoneRepository, results, electricChargingWriter);
		addOpportunities(zoneRepository, numberOfZones);
		ImpedanceIfc localImpedance = impedance(input, matrices, zoneRepository);
		ImpedanceIfc impedance = data.impedance(localImpedance, zoneRepository);
		VehicleBehaviour vehicleBehaviour = data.vehicleBehaviour(results);
		PersonLoader personLoader = personLoader(zoneRepository, numberOfZones);
		return new LocalDataForSimulation(matrices, zoneRepository, impedance, personLoader,
				vehicleBehaviour);
	}

	private ZoneRepository loadZonesFromMobiTopp(Supplier<Network> networkSupplier) {
		ZoneRepositorySerialiser serialisedData = createSerialiser();
		if (serialisedData.isAvailable()) {
			return serialisedData.load();
		}
		Network network = networkSupplier.get();
		return loadZonesFromVisum(network.visumNetwork, network.roadNetwork);
	}

	private ZoneRepositorySerialiser createSerialiser() {
		ChargingDataFactory factory = createChargingFactory();
		return new ZoneRepositorySerialiser(zoneRepositoryFolder, factory, attractivityDataFile);
	}

	private ChargingDataFactory createChargingFactory() {
		return charging.factory(defaultPower());
	}

	private PersonLoader personLoader(ZoneRepository zoneRepository, int numberOfZones) {
		try {
			return loadFromLocalFolder(zoneRepository, numberOfZones);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private PersonLoader loadFromLocalFolder(ZoneRepository zoneRepository, int numberOfZones)
			throws Exception {
		DemandDataFolder demandDataFolder = demandDataFolder(zoneRepository, numberOfZones);
		try (DemandDataDeserialiser deserialiser = demandDataFolder.deserialiseFromCsv()) {
			Population population = deserialiser.loadPopulation();
			return new LocalPersonLoader(population);
		}
	}

	@Override
	public void validate() {
		validateFiles();
		validateMatrices();
	}

	private void validateFiles() {
		validateDemandDataFolder();
		validateZoneRepositoryFolder();
		Validate.files(matrixConfigurationFile).doExist();
	}

	private void validateZoneRepositoryFolder() {
		if (zoneRepositoryFolder.exists()) {
			return;
		}
		System.out.println("Zone repository folder is missing. It will be created at: "
				+ zoneRepositoryFolder.getAbsolutePath());
		zoneRepositoryFolder.mkdirs();
	}

	private void validateDemandDataFolder() {
		if (demandDataFolder.exists()) {
			return;
		}
		System.out.println("Demand data folder is missing. It will be created at: "
				+ demandDataFolder.getAbsolutePath());
		demandDataFolder.mkdirs();
	}

	private void validateMatrices() {
		try {
			matrixConfiguration().validate();
		} catch (FileNotFoundException cause) {
			throw new UncheckedIOException("Missing file check for matrix configuration.", cause);
		}
	}

	@Override
	public String toString() {
		return getClass().getName() + " [matrixConfigurationFile=" + matrixConfigurationFile
				+ ", demandDataFolder=" + demandDataFolder + ", attractivityDataFile="
				+ attractivityDataFile + ", charging=" + charging + ", defaultChargingPower="
				+ defaultChargingPower + "]";
	}
}
