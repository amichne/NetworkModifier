package InMemMatsim.Model;

import InMemMatsim.InMemScenarioUtils;
import InMemMatsim.Model.Specification.Specification;
import com.sun.deploy.security.SelectableSecurityManager;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.core.config.Config;
import org.matsim.core.controler.Controler;
import org.matsim.core.network.NetworkChangeEvent;
import org.matsim.core.scenario.MutableScenario;

import java.util.List;


import static InMemMatsim.InMemNetworkEvents.readNetworkChangeEvents;
import static InMemMatsim.InMemNetworkUtils.*;
import static InMemMatsim.InMemScenarioUtils.loadPopulationFromPlans;

public class Model {
    public static void main(String[] args){
        String path = "/Users/austinmichne/Research/ChesterIcarus/NetworkModification/modelSpecifications.xml";
        Model model = new Model(new Specification(path));
        model.simulate();
    }

    private Config config;
    private Network network;
    private List<NetworkChangeEvent> events;
    private MutableScenario scenario;

    /**
     * Creates a model from a specification XML file
     * @param specification - Specification object to create a scenario from
     */
    public Model(Specification specification)  {
        loadScenario(specification);
    }


    public void simulate(){
        Controler controler = new Controler(this.scenario);
        controler.run();
    }

    private void loadScenario(Specification specification){
        config = specification.getConfig();
        network = loadNetwork(config, network, specification.getNetworkPath());
        scenario = InMemScenarioUtils.createScenario(config);
        scenario.setNetwork(network);

        if (specification.getEventPath() != null) {
            if (network == null)
                throw new NullPointerException("Unable to load Network events for an undefined Network.");
            events = (List<NetworkChangeEvent>) readNetworkChangeEvents(network, specification.getEventPath());
        }
        else {
            events = null;
        }

    }

    public Scenario getScenario(){
        return (Scenario) scenario;
    }
}
