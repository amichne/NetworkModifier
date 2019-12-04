package InMemMatsim.Model.Specification.Parameters.Modes.Mode;

import InMemMatsim.Model.Specification.Core.Parameter;
import InMemMatsim.Model.Specification.Core.Parser;
import org.matsim.core.config.Config;
import org.matsim.core.config.groups.PlanCalcScoreConfigGroup;
import org.w3c.dom.Element;

import static InMemMatsim.Model.Specification.Core.Parser.getChild;

public class Mode extends Parameter {
    public String type = "default";
    public double constant = 0.0D;
    public double dailyMonetaryConstant = 0.0D;
    public double dailyUtilityConstant = 0.0D;
    public double marginalUtilityOfDistance = 0.0D;
    public double marginalUtilityOfTraveling = -6.0D;
    public double monetaryDistanceRate = 0.0D;

    public Mode(Element element){
        super();
        populate(this, Parser.getParameters(element, this.getClass()));
    }

    public void toMatsim(Config config){
        PlanCalcScoreConfigGroup.ModeParams params = new PlanCalcScoreConfigGroup.ModeParams(type);
        setMatsimParams(params, this);
        config.planCalcScore().addModeParams(params);
    }
}
