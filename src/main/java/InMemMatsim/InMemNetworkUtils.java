package InMemMatsim;

import org.matsim.api.core.v01.network.Network;
import org.matsim.core.config.Config;
import org.matsim.core.network.NetworkChangeEvent;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.TimeDependentNetwork;

import java.util.List;

public class InMemNetworkUtils {
    private InMemNetworkUtils(){};

    public static Network loadNetwork(Config config, Network network, String networkPath){
        network = NetworkUtils.createNetwork(config);
        NetworkUtils.readNetwork(network, networkPath);
        return network;
    }

    public static Network loadNetworkWithEvents(Config config, Network network, String networkPath, String csvEvents){
        network = NetworkUtils.createNetwork(config);
        NetworkUtils.readNetwork(network, networkPath);
        InMemNetworkUtils.setNetworkEvents(network, csvEvents);
        return network;
    }

    public static void setNetworkEvents(Network network, String csvEvents){
        List<NetworkChangeEvent> events = (List<NetworkChangeEvent>) InMemNetworkEvents.getNetworkEvents(network, csvEvents);
        NetworkUtils.setNetworkChangeEvents(network, events);
    }

    public static void setNetworkEvents(TimeDependentNetwork network, String csvEvents){
        setNetworkEvents((Network) network, csvEvents);
    }
}
