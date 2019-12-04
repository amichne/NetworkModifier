package InMemMatsim.Model.Specification.Parameters;

import InMemMatsim.Model.Specification.Core.Parameter;
import InMemMatsim.Model.Specification.Core.Parser;
import org.matsim.core.config.Config;
import org.w3c.dom.Element;

import static InMemMatsim.Model.Specification.Core.Parser.*;

public class GlobalParameters extends Parameter {
    public int threads;

    public GlobalParameters(Element element){
        super();
        populate(this, Parser.getParameters(getChild(element, "global"), this.getClass()));

    }

    public void toMatsim(Config config){
        config.global().setNumberOfThreads(threads);

    }
}
