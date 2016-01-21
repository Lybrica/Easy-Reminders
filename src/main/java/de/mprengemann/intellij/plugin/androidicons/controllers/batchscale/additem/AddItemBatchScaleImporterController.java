package de.mprengemann.intellij.plugin.androidicons.controllers.batchscale.additem;

import com.google.common.base.Objects;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import de.mprengemann.intellij.plugin.androidicons.images.ResizeAlgorithm;
import de.mprengemann.intellij.plugin.androidicons.model.ImageInformation;
import de.mprengemann.intellij.plugin.androidicons.model.Resolution;
import de.mprengemann.intellij.plugin.androidicons.util.ExportNameUtils;
import de.mprengemann.intellij.plugin.androidicons.util.RefactorUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AddItemBatchScaleImporterController implements IAddItemBatchScaleImporterController {

    private Set<AddItemBatchScaleDialogObserver> observers;
    private Set<Resolution> targetResolutions;
    private Resolution sourceResolution;
    private String exportName;
    private File imageFile;
    private int originalImageWidth;
    private int targetWidth;
    private int targetHeight;
    private String exportRoot;
    private ResizeAlgorithm algorithm;
    private String method;
    private float aspectRatio;
    private boolean isNinePatch;

    public AddItemBatchScaleImporterController(VirtualFile root, File file) {
        this.observers = new HashSet<AddItemBatchScaleDialogObserver>();
        this.targetResolutions = new HashSet<Resolution>(Arrays.asList(Resolution.values()));
        init(file);

        final String fileName = file.getName();
        exportName = ExportNameUtils.getExportNameFromFilename(fileName);
        sourceResolution = Resolution.XHDPI;
        algorithm = ResizeAlgorithm.SCALR;
        method = algorithm.getMethods().get(0);
        if (root != null) {
            exportRoot = root.getCanonicalPath();
        } else {
            exportRoot = "";
        }
        isNinePatch = fileName.endsWith(".9.png");
    }

    public AddItemBatchScaleImporterController(Resolution sourceResolution, List<ImageInformation> information) {
        this.observers = new HashSet<AddItemBatchScaleDialogObserver>();
        this.targetResolutions = new HashSet<Resolution>();
        for (ImageInformation imageInformation : information) {
            targetResolutions.add(imageInformation.getResolution());
        }
        final ImageInformation baseinformation = information.get(0);
        init(baseinformation.getImageFile());

        this.exportName = baseinformation.getExportName();
        this.sourceResolution = sourceResolution;
        this.algorithm = baseinformation.getAlgorithm();
        this.method = algorithm.getPrettyMethod(baseinformation.getMethod());
        this.exportRoot = baseinformation.getExportPath();
        this.isNinePatch = baseinformation.isNinePatch();

        this.targetHeight = getOriginalTargetSize(sourceResolution, baseinformation.getResolution(), targetHeight, baseinformation.getFactor());
        this.targetWidth = getOriginalTargetSize(sourceResolution, baseinformation.getResolution(), targetWidth, baseinformation.getFactor());
    }

    private void init(File file) {
        try {
            BufferedImage image = ImageIO.read(file);
            imageFile = file;
            originalImageWidth = image.getWidth();
            targetWidth = image.getWidth();
            targetHeight = image.getHeight();
            aspectRatio = (float) image.getHeight() / (float) originalImageWidth;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getExportName() {
        return exportName;
    }

    @Override
    public String getExportPath() {
        return exportRoot;
    }

    @Override
    public Set<Resolution> getTargetResolutions() {
        return targetResolutions;
    }

    @Override
    public int getTargetWidth() {
        return targetWidth;
    }

    @Override
    public int getTargetHeight() {
        return targetHeight;
    }

    @Override
    public ResizeAlgorithm getAlgorithm() {
        return algorithm;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public File getImageFile() {
        return imageFile;
    }

    @Override
    public void setSourceResolution(Resolution resolution) {
        sourceResolution = resolution;
        notifyUpdated();
    }

    @Override
    public Resolution getSourceResolution() {
        return sourceResolution;
    }

    @Override
    public void addTargetResolution(Resolution resolution) {
        if (targetResolutions.contains(resolution)) {
            return;
        }
        targetResolutions.add(resolution);
        notifyUpdated();
    }

    @Override
    public void removeTargetResolution(Resolution resolution) {
        if (!targetResolutions.contains(resolution)) {
            return;
        }
        targetResolutions.remove(resolution);
        notifyUpdated();
    }

    @Override
    public void setTargetHeight(int height) {
        if (targetHeight == height) {
            return;
        }
        targetHeight = height;
        targetWidth = (int) (targetHeight / aspectRatio);
        notifyUpdated();
    }

    @Override
    public void setTargetWidth(int width) {
        if (targetWidth == width) {
            return;
        }
        targetWidth = width;
        targetHeight = (int) (aspectRatio * targetWidth);
        notifyUpdated();
    }

    @Override
    public void setTargetRoot(String path) {
        if (exportRoot.equals(path)) {
            return;
        }
        exportRoot = path;
        notifyUpdated();
    }

    @Override
    public void setTargetName(String name) {
        if (exportName.equals(name)) {
            return;
        }
        exportName = name;
        notifyUpdated();
    }

    @Override
    public void setAlgorithm(ResizeAlgorithm algorithm) {
        if (algorithm == this.algorithm) {
            return;
        }
        this.algorithm = algorithm;
        this.method = algorithm.getMethods().get(0);
        notifyUpdated();
    }

    @Override
    public void setMethod(String method) {
        if (Objects.equal(method, this.method)) {
            return;
        }
        this.method = method;
        notifyUpdated();
    }

    @Override
    public List<String> getMethods() {
        return algorithm.getMethods();
    }

    @Override
    public int[] getScaledSize(Resolution resolution) {
        final float scaleFactor = RefactorUtils.getScaleFactor(resolution, sourceResolution);
        return new int[] {(int) (scaleFactor * targetWidth), (int) (scaleFactor * targetHeight)};
    }

    @Override
    public List<ImageInformation> getImageInformation(Project project) {
        final ImageInformation base = ImageInformation.newBuilder()
                                                      .setExportName(exportName)
                                                      .setImageFile(imageFile)
                                                      .setAlgorithm(algorithm)
                                                      .setMethod(algorithm.getMethod(method))
                                                      .setExportPath(exportRoot)
                                                      .setNinePatch(isNinePatch)
                                                      .build();
        final List<ImageInformation> images = new ArrayList<ImageInformation>();
        for (Resolution resolution : targetResolutions) {
            images.add(ImageInformation.newBuilder(base)
                                       .setResolution(resolution)
                                       .setFactor(getRealScaleFactor(resolution))
                                       .build());
        }
        return images;
    }

    private float getRealScaleFactor(Resolution targetResolution) {
        final float resolutionScaleFactor = RefactorUtils.getScaleFactor(targetResolution, sourceResolution);
        final float sizeScaleFactor = (float) targetWidth / (float) originalImageWidth;
        return resolutionScaleFactor * sizeScaleFactor;
    }

    private int getOriginalTargetSize(Resolution sourceResolution, Resolution targetResolution, int size, float factor) {
        final float resolutionScaleFactor = RefactorUtils.getScaleFactor(targetResolution, sourceResolution);
        final float sizeScaleFactor = factor / resolutionScaleFactor;
        return (int) (sizeScaleFactor * size);
    }

    @Override
    public void addObserver(AddItemBatchScaleDialogObserver observer) {
        observers.add(observer);
        notifyUpdated();
    }

    @Override
    public void removeObserver(AddItemBatchScaleDialogObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void tearDown() {
        observers.clear();
    }

    private void notifyUpdated() {
        for (AddItemBatchScaleDialogObserver observer : observers) {
            observer.updated();
        }
    }
}
