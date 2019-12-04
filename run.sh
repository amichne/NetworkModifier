java -Djava.awt.headless=true -Xms1g -Xmx12g \
    -cp './target/NetworkModification-jar-with-dependencies.jar' \
#     InMemMatsim.MetaModel.MetaModel \
    InMemMatsim.Model.Model \
    'data/scenarios/scenario_5b'
