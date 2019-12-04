package InMemMatsim.MetaModel;

import InMemMatsim.Model.Model;
import InMemMatsim.Model.Specification.Specification;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MetaModelUtils {

    public static List<Model> createModels(String directory) throws IOException {
        List<Model> models = new ArrayList<>();
        for (String childDir : getChildDirs(directory)){
            models.add(new Model(new Specification(getSpecificationFile(childDir))));
        }
        return models;
    }

    public static List<String> getChildDirs(String parentDir) throws IOException {
        Stream<Path> walk = Files.walk(Paths.get(parentDir));
        List<String> subDirs = walk.filter(Files::isDirectory).map(
                Path::toString).collect(Collectors.toList());
        subDirs.remove(parentDir);
        subDirs.sort(Comparator.naturalOrder());

        return subDirs;
    }

    public static List<String> getChildDirs(String parentDir, String filter) throws IOException {
        List<String> filtered = new ArrayList<>();
        for (String dir : getChildDirs(parentDir))
            if (!dir.toLowerCase().contains(filter.toLowerCase()))
                filtered.add(dir);
        return filtered;
    }


    /**
     * Returns the specification file given the path to an absolute directory
     * @param path
     * @return
     */
    public static String getSpecificationFile(String path) throws IOException {
        return getInputFileFromDir(path, "specification", true);
    }


    public static String getInputFileFromDir(String path, String indicator, boolean ignoreCase) throws IOException {
        Stream<Path> stream = null;
        try { stream = Files.walk(Paths.get(path)); } 
        catch (IOException e) { e.printStackTrace(); } 
        assert stream != null;
        
        List<String> files = stream.filter(Files::isRegularFile).map(Path::toString).collect(Collectors.toList());
        String specFile = null;

        for (String file : files){
            String tmpFile;
            String tmpIndicator;

            if (ignoreCase) { tmpFile = file.toLowerCase();tmpIndicator = indicator.toLowerCase(); }
            else { tmpFile = file;tmpIndicator = indicator; }

            if (tmpFile.contains(tmpIndicator)) {
                if (specFile == null)
                    specFile = file;
                else
                    throw new IOException("Multiple valid " + indicator + " files found in directory: " + path);
            }
        }
        if (specFile == null)
            throw new IOException("No valid " + indicator + " files found in directory: " + path);
        return specFile;
    }



}
