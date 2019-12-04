package InMemMatsim.MetaModel;


import InMemMatsim.Model.Model;

import java.io.IOException;
import java.util.List;

public class MetaModel {
    private final String topLevelDir = null;

    public MetaModel(){};

    public static void main(String[] args){
        // String path = "/Users/austinmichne/Research/ChesterIcarus/NetworkModification/data/scenarios";
        List<Model> models;
        try {
            models = MetaModelUtils.createModels(args[0]);
            for (Model model : models){
                model.simulate();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

}
