package nb7232.muc_hw1;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import nb7232.muc_hw1.database.LocationDbHelper;
import si.uni_lj.fri.lrss.machinelearningtoolkit.MachineLearningManager;
import si.uni_lj.fri.lrss.machinelearningtoolkit.classifier.Classifier;
import si.uni_lj.fri.lrss.machinelearningtoolkit.utils.ClassifierConfig;
import si.uni_lj.fri.lrss.machinelearningtoolkit.utils.Constants;
import si.uni_lj.fri.lrss.machinelearningtoolkit.utils.Feature;
import si.uni_lj.fri.lrss.machinelearningtoolkit.utils.FeatureNominal;
import si.uni_lj.fri.lrss.machinelearningtoolkit.utils.FeatureNumeric;
import si.uni_lj.fri.lrss.machinelearningtoolkit.utils.Instance;
import si.uni_lj.fri.lrss.machinelearningtoolkit.utils.MLException;
import si.uni_lj.fri.lrss.machinelearningtoolkit.utils.Signature;
import si.uni_lj.fri.lrss.machinelearningtoolkit.utils.Value;

/**
 * Created by nejc on 7.11.2015.
 */
public class MachineLearning extends IntentService{

    final public static String WORK = "work";
    final public static String HOME = "home";

    private Context mContext;

    private MachineLearningManager mlm;

    public MachineLearning() {
        super("MachineLearning");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        mContext = getApplicationContext();
        try {
            Log.e("MachineLearning", "onHandleIntent");
            mlm = MachineLearningManager
                    .getMLManager(mContext);

            Log.e("MachineLearning", "configureMl");
            configureML();
            Log.e("MachineLearning", "classificationTest");
            clasificationTest();
        } catch (Exception e) {
            Log.e("MachineLearning", "Error instantiation MachineLearningManager");
        }
    }

    public void configureML() {
        Log.e("MachineLearning", "configureML()");

        Feature longitude = new FeatureNumeric("longitude");
        Feature latitude = new FeatureNumeric("latitude");
        ArrayList<String> locationValues = new ArrayList<String>();
        locationValues.add(WORK);
        locationValues.add(HOME);
        Log.e("MachineLearning", "featureNormal");
        Feature location = new FeatureNominal("location", locationValues);
        ArrayList<Feature> features = new ArrayList<Feature>();
        features.add(longitude);
        features.add(latitude);
        features.add(location);

        Log.e("MachineLearning", "new signature");
        Signature signature = new Signature(features, features.size() - 1);
        ClassifierConfig config = new ClassifierConfig();
        config.addParam(
                si.uni_lj.fri.lrss.machinelearningtoolkit.utils.Constants.MAX_CLUSTER_DISTANCE, 0.5);
        config.addParam(si.uni_lj.fri.lrss.machinelearningtoolkit.utils.Constants.MIN_INCLUSION_PERCENT, 0.5);

        try {
            Log.e("MachineLearning", "addclassifier");
            Classifier cls = mlm.addClassifier(
                    Constants.TYPE_DENSITY_CLUSTER,
                    signature, config,
                    "location");

            Log.e("MachineLearning", "train!!!");

            cls.train(populateWithLabeledData());

        } catch (Exception e) {
            Log.e("configureML", e.getMessage());
        }
    }

    public ArrayList<Instance> populateWithLabeledData() {
        ArrayList<Instance> instanceQ = new ArrayList<Instance>();


        LocationDbHelper ldh = new LocationDbHelper(mContext);
        String whereClause = "label IS NOT NULL";
        Cursor cursor = ldh.getReadableDatabase().query("location", null, whereClause, null,
                null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ArrayList<Value> instanceValues = new ArrayList<Value>();
            instanceValues.add(new Value(cursor.getDouble(cursor.getColumnIndex("latitude")), Value.NUMERIC_VALUE));
            instanceValues.add(new Value(cursor.getDouble(cursor.getColumnIndex("latitude")), Value.NUMERIC_VALUE));
            instanceValues.add(new Value(cursor.getString(cursor.getColumnIndex("label")), Value.NOMINAL_VALUE));

            instanceQ.add(new Instance(instanceValues));

            cursor.moveToNext();
        }

        return instanceQ;

    }

    public void clasificationTest() {
        ArrayList<Value> instanceValues = new ArrayList<Value>();
        instanceValues.add(new Value(46.0495558, Value.NUMERIC_VALUE));
        instanceValues.add(new Value(14.4598184, Value.NUMERIC_VALUE));
        Instance instance = new Instance(instanceValues);
        Log.e("MachineLearning", "instance created!");

        try {

            Log.e("classificationTest",mlm.getClassifier("location").classify(instance).getValue().toString());
            Log.e("MachineLearning", "the end");

        } catch (MLException mle) {
            Log.e("MachineLearning", mle.getMessage());
        }
    }
}
