package InMemMatsim.Model.Specification.Parameters;

import InMemMatsim.Model.Specification.Core.Parser;
import InMemMatsim.Model.Specification.Parameters.Activities.Activities;
import InMemMatsim.Model.Specification.Parameters.Modes.Modes;
import InMemMatsim.Model.Specification.Core.Parameter;
import org.matsim.core.config.Config;
import org.w3c.dom.Element;

import static InMemMatsim.Model.Specification.Core.Parser.getChild;

public class PlanParameters extends Parameter {
    public Modes modes;
    public Activities activities;
    /* TODO: Add PlanScoring module to parser*/

    public PlanParameters(Element element){
        super();
        populate(this, Parser.getParameters(getChild(element, "planParameters"), this.getClass()));
        this.modes = new Modes(element);
        this.activities = new Activities(element);
    }

    public void toMatsim(Config config){
        modes.toMatsim(config);
        activities.toMatsim(config);
    }
}
