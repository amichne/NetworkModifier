package InMemMatsim.Model.Specification;

import InMemMatsim.Model.Specification.Parameters.*;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static InMemMatsim.Model.Specification.Core.Parser.getChild;
import static InMemMatsim.Model.Specification.Core.Parser.getParameterElement;

public class Specification {
    private String specificationPath;
    private Element rootElement;
    private String configPath;
    private String networkPath;
    private String planPath;
    private String eventPath;
    private Config config;
    private NetworkParameters networkParameters;
    private PlanParameters planParameters;
    private QSimParameters qSimParameters;
    private ControlerParameters controlerParameters;
    private GlobalParameters globalParameters;

    public static void main(String[] args) {
        String path = "/Users/austinmichne/Research/ChesterIcarus/NetworkModification/modelSpecifications.xml";
        Specification specification = new Specification(path);
        String configPath = "/Users/austinmichne/Research/ChesterIcarus/NetworkModification/exampleConfig.xml";
        ConfigUtils.writeMinimalConfig(specification.getConfig(), configPath);
    }

    public Specification(String path) {
        specificationPath = path;
        rootElement = loadSpecification(specificationPath);
        loadMeta(rootElement);
        generateConfig();
        processParameters();
    }

    private Element loadSpecification(String filepath) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);
        Document document = null;
        try {
            document = factory.newDocumentBuilder().parse(new File(filepath));
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        assert document != null;
        document.normalizeDocument();
        return document.getDocumentElement();
    }

    private void loadMeta(Element rootElement) {
        Element metaElement = null;
        metaElement = getChild(rootElement, "specificationMeta");
        planPath = getChild(metaElement, "plans").getAttribute("path");
        networkPath = getChild(metaElement, "inputNetwork").getAttribute("path");
        configPath = getChild(metaElement, "config").getAttribute("path");
        eventPath = getChild(metaElement, "events").getAttribute("path");
        List<String> paths = Arrays.asList(planPath, networkPath);

        if (paths.contains(null) || paths.contains("")) {
            try {
                throw new InstantiationException("One or more of the paths in specificationMeta does not exist." +
                        "\nThe following elements are required: plans, network");
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    private void generateConfig() {
        if (configPath == null)
            config = ConfigUtils.createConfig();
        else {
            config = ConfigUtils.loadConfig(configPath);
            config.network().setChangeEventsInputFile(null);
            config.network().setInputFile(null);
            config.plans().setInputFile(null);
        }
    }

    private void processParameters() {
        networkParameters = new NetworkParameters(rootElement);
        planParameters = new PlanParameters(rootElement);
        qSimParameters = new QSimParameters(rootElement);
        controlerParameters = new ControlerParameters(rootElement);
        globalParameters = new GlobalParameters(rootElement);
    }

    public Config getConfig() {
        networkParameters.toMatsim(config);
        planParameters.toMatsim(config);
        qSimParameters.toMatsim(config);
        controlerParameters.toMatsim(config);
        globalParameters.toMatsim(config);
        return config;
    }

    public String getSpecificationPath() {
        return specificationPath;
    }

    public String getPlanPath() {
        return planPath;
    }

    public String getEventPath() {
        return eventPath;
    }

    public String getNetworkPath() {
        return networkPath;
    }

    public String getConfigPath() {
        return configPath;
    }

}
