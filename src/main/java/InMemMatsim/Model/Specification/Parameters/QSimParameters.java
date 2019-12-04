package InMemMatsim.Model.Specification.Parameters;

import InMemMatsim.Model.Specification.Core.Parameter;
import InMemMatsim.Model.Specification.Core.Parser;
import org.matsim.core.config.Config;
import org.matsim.core.config.groups.QSimConfigGroup;
import org.w3c.dom.Element;

import java.util.Arrays;
import java.util.Collection;

import static InMemMatsim.Model.Specification.Core.Parser.getChild;
import static InMemMatsim.Model.Specification.Core.Parser.getParameterElement;

public class QSimParameters extends Parameter {
    public double startTime = 0.0D;
    public double endTime = 0.0D;
    public double timeStepSize = 1.0D;
    public double snapshotPeriod = 0.0D;
    public double flowCapFactor = 1.0D;
    public double storageCapFactor = 1.0D;
    public double stuckTime = 10.0D;

    public boolean removeStuckVehicles = false;
    public boolean usePersonIdForMissingVehicleId = true;
    public int numberOfThreads = 1;

    public double nodeOffset = 0.0D;
    public boolean insertingWaitingVehiclesBeforeDrivingVehicles = true;
    public boolean usingThreadpool = true;
    public boolean useLanes = false;
    public boolean usingFastCapacityUpdate = true;
    public boolean isSeepModeStorageFree = false;

    public Collection<String> mainModes = Arrays.asList("car");
    public Collection<String> seepModes = Arrays.asList("bike");
    public QSimConfigGroup.StarttimeInterpretation simStarttimeInterpretation = QSimConfigGroup.StarttimeInterpretation.onlyUseStarttime;
    public QSimConfigGroup.EndtimeInterpretation simEndtimeInterpretation = QSimConfigGroup.EndtimeInterpretation.onlyUseEndtime;
    public QSimConfigGroup.TrafficDynamics trafficDynamics = QSimConfigGroup.TrafficDynamics.queue;
    public QSimConfigGroup.VehicleBehavior vehicleBehavior = QSimConfigGroup.VehicleBehavior.teleport;
    public QSimConfigGroup.VehiclesSource vehiclesSource = QSimConfigGroup.VehiclesSource.defaultVehicle;
    public QSimConfigGroup.SnapshotStyle snapshotStyle = QSimConfigGroup.SnapshotStyle.equiDist;
    public QSimConfigGroup.LinkDynamics linkDynamics = QSimConfigGroup.LinkDynamics.FIFO;

    public QSimParameters(Element element){
        super();
        /* TODO: Finish this. Standard parsing for primitives, fall back on MATsim *.valueOf funcs for QSimConfigGroup.**/
            populate(this,
                    Parser.getParameters(getChild(element, "qsim"), this.getClass()));

//        catch (InstantiationException e){
//            e.printStackTrace();
//            System.exit(1);
//        }
        System.out.println("Test");
    }

    public void toMatsim(Config config) {
        config.qsim().setStartTime(startTime);
        config.qsim().setEndTime(endTime);
        config.qsim().setTimeStepSize(timeStepSize);
        config.qsim().setSnapshotPeriod(snapshotPeriod);
        config.qsim().setFlowCapFactor(flowCapFactor);
        config.qsim().setStorageCapFactor(storageCapFactor);
        config.qsim().setStuckTime(stuckTime);
        config.qsim().setRemoveStuckVehicles(removeStuckVehicles);
        config.qsim().setUsePersonIdForMissingVehicleId(usePersonIdForMissingVehicleId);
        config.qsim().setNumberOfThreads(numberOfThreads);
        config.qsim().setNodeOffset(nodeOffset);
        config.qsim().setInsertingWaitingVehiclesBeforeDrivingVehicles(insertingWaitingVehiclesBeforeDrivingVehicles);
        config.qsim().setUsingThreadpool(usingThreadpool);
        config.qsim().setUseLanes(useLanes);
        config.qsim().setUsingFastCapacityUpdate(usingFastCapacityUpdate);
        config.qsim().setSeepModeStorageFree(isSeepModeStorageFree);
    }
}
