package InMemMatsim;

import org.matsim.api.core.v01.network.Network;
import org.matsim.core.config.Config;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.TimeDependentNetwork;
import org.matsim.core.population.io.PopulationReader;
import org.matsim.core.scenario.MutableScenario;
import org.matsim.core.scenario.ScenarioUtils;


public class InMemScenarioUtils {
    /* TODO: Expand functionality - not sure how but need to learn more about MATsim Scenario class */

    /**
     * Creates a mutable scenario from a config.
     * @param config
     * @return
     */
    public static MutableScenario createScenario(Config config){
        return ScenarioUtils.createMutableScenario(config);
    }

    /**
     * Loads a Network into the given MutableScenario
     * @param scenario
     * @param network
     */
    public static void setNetwork(MutableScenario scenario, Network network){
        scenario.setNetwork(network);
    }
    public static void setNetwork(MutableScenario scenario, TimeDependentNetwork network){
        setNetwork(scenario, (Network) network);
    }
    public static void setNetwork(MutableScenario scenario, String string){
        setNetwork(scenario, NetworkUtils.readNetwork(string));
    }

    /**
     * Loads the population directly - getting around all of the relative path fucker-y in MATsim
     * @param scenario
     * @param filepath
     */
    public static void loadPopulationFromPlans(MutableScenario scenario, String filepath){
        /* TODO: Confirm this works for bypassing the plans in the Config given to create the original scenario, that's its' true purpose */
        PopulationReader reader = new PopulationReader(scenario);
        reader.readFile(filepath);
    }
}
