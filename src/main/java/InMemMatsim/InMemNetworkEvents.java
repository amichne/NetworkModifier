package InMemMatsim;

import com.opencsv.CSVReader;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.core.config.Config;
import org.matsim.core.network.NetworkChangeEvent;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.TimeDependentNetwork;
import org.matsim.core.network.io.NetworkChangeEventsWriter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class InMemNetworkEvents {

    private InMemNetworkEvents(){};

    /**
     * Parses a CSV file of raw Network Events, and returns a collection of MATsim NetworkChangeEvent
     * @param network
     * @param csvEvents
     * @return
     */
    public static Collection<NetworkChangeEvent> getNetworkEvents(Network network, String csvEvents){
        return readNetworkChangeEvents(network, csvEvents);
    }

    public static Collection<NetworkChangeEvent> getNetworkEvents(TimeDependentNetwork network, String csvEvents){
        return getNetworkEvents((Network) network, csvEvents);
    }


    public static Collection<NetworkChangeEvent> getNetworkEvents(String string, String csvEvents){
        return getNetworkEvents(NetworkUtils.readNetwork(string), csvEvents);
    }

    public static Collection<NetworkChangeEvent> getNetworkEvents(Config config, String csvEvents){
        return getNetworkEvents(config.network().getInputFile(), csvEvents);
    }

    public static void writeNetworkEvents(String path, Collection<NetworkChangeEvent> events){
        NetworkChangeEventsWriter writer = new NetworkChangeEventsWriter();
        writer.write(path, events);
    }

    private static final int LINK_STR = 0;
    private static final int START_TIME = 1;
    private static final int FLOW = 2;
    private static final int FREESPEED = 3;
    private static final int LANES = 4;

    /**
     * Reads the network change events from a properly formatted CSV file.
     * @param string
     * @return
     */
    public static Collection<NetworkChangeEvent> readNetworkChangeEvents(Network network, String string) {
        Collection<NetworkChangeEvent> events = new ArrayList<>();
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(string);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        CSVReader csvReader = new CSVReader(fileReader);
        String[] nextRecord;
        while (true){
            try {
                if ((nextRecord = csvReader.readNext()) != null)
                    events.add(readChangeEventFromLine(network, nextRecord));
                else break;
            } catch (IOException e) { e.printStackTrace(); }
        }
        return events;
    }

    public static NetworkChangeEvent readChangeEventFromLine(Network network, String[] strings) throws IllegalArgumentException {
        NetworkChangeEvent event = new NetworkChangeEvent(Double.parseDouble(strings[START_TIME]));
        Link link = network.getLinks().get(Id.createLinkId(strings[LINK_STR]));
        event.addLink(link);

        boolean valid = false;
        if (!strings[FLOW].equalsIgnoreCase("null")){
            event.setFlowCapacityChange(
                    new NetworkChangeEvent.ChangeValue(NetworkChangeEvent.ChangeType.ABSOLUTE_IN_SI_UNITS,
                            Double.parseDouble(strings[FLOW])));
            valid = true;
        }
        if (!strings[FREESPEED].equalsIgnoreCase("null")){
            event.setFreespeedChange(new NetworkChangeEvent.ChangeValue(NetworkChangeEvent.ChangeType.ABSOLUTE_IN_SI_UNITS,
                            Double.parseDouble(strings[FREESPEED])));
            valid = true;
        }
        if (!strings[LANES].equalsIgnoreCase("null")){
            event.setLanesChange(new NetworkChangeEvent.ChangeValue(NetworkChangeEvent.ChangeType.ABSOLUTE_IN_SI_UNITS,
                            Double.parseDouble(strings[LANES])));
            valid = true;
        }
        if (!valid){
            throw new IllegalArgumentException();
        }
        return event;
    }
}
