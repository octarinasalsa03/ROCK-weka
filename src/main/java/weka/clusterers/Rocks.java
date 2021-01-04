package weka.clusterers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import weka.classifiers.rules.DecisionTableHashKey;
import weka.core.Attribute;
import weka.core.Capabilities;
import weka.core.Capabilities.Capability;
import weka.core.DenseInstance;
import weka.core.DistanceFunction;
import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.ManhattanDistance;
import weka.core.Option;
import weka.core.OptionHandler;
import weka.core.RevisionUtils;
import weka.core.SelectedTag;
import weka.core.Tag;
import weka.core.TechnicalInformation;
import weka.core.TechnicalInformation.Field;
import weka.core.TechnicalInformation.Type;
import weka.core.TechnicalInformationHandler;
import weka.core.Utils;
import weka.core.WeightedInstancesHandler;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;


public class Rocks extends AbstractClusterer implements
        OptionHandler, NumberOfClustersRequestable, WeightedInstancesHandler,
        TechnicalInformationHandler {

    static final long serialVersionUID = 1L;

    Instances m_instances;
    private int m_NumClusters;
    private double m_numEps;
    private double m_numThreshold;

    protected DistanceFunction m_DistanceFunction = new EuclideanDistance();

    public DistanceFunction getDistanceFunction() {
        return m_DistanceFunction;
    }

    public void setDistanceFunction(DistanceFunction distanceFunction) {
        m_DistanceFunction = distanceFunction;
    }

    public Rocks() {
        m_NumClusters = 2;
        m_numEps = 0.2;
        m_numThreshold = 0.5;
    }

    @Override
    public TechnicalInformation getTechnicalInformation() {
        TechnicalInformation result;

        result = new TechnicalInformation(Type.INPROCEEDINGS);
        result.setValue(Field.AUTHOR, "Sudipto Guha, Rajeev Rastogi and Kyuseok Shim");
        result.setValue(Field.TITLE,
                "ROCK: A Robust Clustering Algorithm for Categorical Atributes");
        result.setValue(Field.YEAR, "2000");
        result.setValue(Field.PAGES, "345-366");

        return result;
    }

    /**
     * Returns a string describing this clusterer.
     *
     * @return a description of the evaluator suitable for displaying in the
     *         explorer/experimenter gui
     */
    public String globalInfo() {
        return "Cluster data using the ROCK algorithm."
                + " For more information see:\n\n" + getTechnicalInformation().toString();
    }

    public static void main(String[] args) {
        runClusterer(new Rocks(), args);
    }

    @Override
    public double[] distributionForInstance(Instance instance) throws Exception {
        if (numberOfClusters() == 0) {
            double[] p = new double[1];
            p[0] = 1;
            return p;
        }
        double[] p = new double[numberOfClusters()];
        p[clusterInstance(instance)] = 1.0;
        return p;
    }

    @Override
    public Capabilities getCapabilities() {
        Capabilities result = super.getCapabilities();
        result.disableAll();
        result.enable(Capability.NO_CLASS);

        // attributes
        result.enable(Capability.NOMINAL_ATTRIBUTES);
        result.enable(Capability.MISSING_VALUES);

        return result;
    }

    @Override
    public void buildClusterer(Instances data) throws Exception {
        m_instances = data;
        double degree_norm = 0;
        degree_norm = 1.0 + 2.0 * ((1.0 - m_numThreshold) / (1.0 + m_numThreshold));

        int nInstances = m_instances.numInstances();
        m_DistanceFunction.setInstances(m_instances);
        @SuppressWarnings("unchecked")
        Vector<Integer>[] clusters = new Vector[data.numInstances()];
        for (int i = 0; i < data.numInstances(); i++) {
            clusters[i] = new Vector<Integer>();
            clusters[i].add(i);
        }

        int data_size = clusters.length;

        double[] adj_matrix;
        for(int i = 0; i < data_size; i++) {
            for(int j = i+1; j < data_size; j++) {
                double distance = euclideanDistance(clusters[i], clusters[j]);
            }
        }
    }

    private double euclideanDistance(Vector<Integer> a, Vector<Integer> b) {
        double sumSquare = 0;
        for(int i = 0; i < a.size(); i++) {
            int a2 = (Integer) a.elementAt(i).intValue();
            int b2 = (Integer) b.elementAt(i).intValue();
            ////int result = (a2 - b2) * 2;
            //sumSquare =+ abs(result);
        }
        return Math.sqrt(sumSquare);
    }

    @Override
    public int numberOfClusters() throws Exception {
        return m_NumClusters;
    }

    @Override
    public void setNumClusters(int n) throws Exception {
        if (n <= 0) {
            throw new Exception("Number of clusters must be > 0");
        }
        m_NumClusters = n;
    }

    @Override
    public String[] getOptions() {

        Vector<String> options = new Vector<String>();

        options.add("-N");
        options.add("" + getNumClusters());

        options.add("-E");
        options.add("" + getNumEps());

        options.add("-T");
        options.add("" + getNumThreshold());

        Collections.addAll(options, super.getOptions());

        return options.toArray(new String[0]);
    }

    public int getNumClusters() {
        return m_NumClusters;
    }

    public double getNumEps() {
        return m_numEps;
    }

    public double setNumEps(double m_numEps) {
        this.m_numEps = m_numEps;
        return m_numEps;
    }

    public double getNumThreshold() {
        return m_numThreshold;
    }

    public double setNumThreshold(double m_numThreshold) {
        this.m_numThreshold = m_numThreshold;
        return m_numThreshold;
    }

    private int clusterProcessedInstance(Instance instance, boolean updateErrors,
                                         boolean useFastDistCalc, long[] instanceCanopies) {
        double minDist = Integer.MAX_VALUE;
        int bestCluster = 0;

        return bestCluster;
    }

    public int clusterInstance(Instance instance) throws Exception {
        Instance inst = null;

        inst = instance;

        return clusterProcessedInstance(inst, false, true, null);
    }



}