package InMemMatsim.Model.Specification.Parameters;

import InMemMatsim.Model.Specification.Core.Parameter;
import InMemMatsim.Model.Specification.Core.Parser;
import org.matsim.core.config.Config;
import org.w3c.dom.Element;

import static InMemMatsim.Model.Specification.Core.Parser.getChild;
import static InMemMatsim.Model.Specification.Core.Parser.getParameterElement;

public class NetworkParameters extends Parameter {
    public String path;
    public boolean timeVariantNetwork;

    public NetworkParameters(Element element){
        super();
        /* TODO: Finish this. Standard parsing for primitives, fall back on MATsim *.valueOf funcs for QSimConfigGroup.**/
        populate(this, Parser.getParameters(getChild(element, "network"), this.getClass()));

    }

    public void toMatsim(Config config){
        config.network().setTimeVariantNetwork(timeVariantNetwork);
    }
}
