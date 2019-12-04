package InMemMatsim.Model.Specification.Parameters.Activities;

import InMemMatsim.Model.Specification.Core.Parser;
import InMemMatsim.Model.Specification.Parameters.Activities.Activity.Activity;
import InMemMatsim.Model.Specification.Core.Parameter;
import org.matsim.core.config.Config;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

import static InMemMatsim.Model.Specification.Core.Parser.*;

public class Activities extends Parameter {
    public List<Activity> activities;
    public static final Class DESCENDANT = Activity.class;

    public Activities(){
        super();
        this.activities = new ArrayList<>();
    }

    public Activities(Element element){
        this();
        populate(this, Parser.getParameters(getChild(element, "activities"), this.getClass()));
        for (Element childActivity : getChildren(element, getClassName(DESCENDANT)))
            this.activities.add(new Activity(childActivity));
    }

    @Override
    public void toMatsim(Config config) {
        for (Activity activity : activities){
            activity.toMatsim(config);
        }
    }
}
