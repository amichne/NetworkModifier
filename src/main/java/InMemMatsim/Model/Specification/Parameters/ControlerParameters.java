package InMemMatsim.Model.Specification.Parameters;

import InMemMatsim.Model.Specification.Core.Parameter;
import InMemMatsim.Model.Specification.Core.Parser;
import org.matsim.core.config.Config;
import org.w3c.dom.Element;

import static InMemMatsim.Model.Specification.Core.Parser.getChild;
import static InMemMatsim.Model.Specification.Core.Parser.getParameterElement;

public class ControlerParameters extends Parameter {
    public String outputDirectory;

    public ControlerParameters(Element element) {
        super();
        populate(this, Parser.getParameters(getChild(element, "controler"), this.getClass()));

    }

    public void toMatsim(Config config) {
        config.controler().setOutputDirectory(outputDirectory);
    }
}
