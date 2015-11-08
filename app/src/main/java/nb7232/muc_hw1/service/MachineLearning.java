package nb7232.muc_hw1.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nb7232.muc_hw1.database.LocationDbHelper;
import si.uni_lj.fri.lrss.machinelearningtoolkit.MachineLearningManager;
import si.uni_lj.fri.lrss.machinelearningtoolkit.classifier.DensityClustering;
import si.uni_lj.fri.lrss.machinelearningtoolkit.utils.ClassifierConfig;
import si.uni_lj.fri.lrss.machinelearningtoolkit.utils.Constants;
import si.uni_lj.fri.lrss.machinelearningtoolkit.utils.Feature;
import si.uni_lj.fri.lrss.machinelearningtoolkit.utils.FeatureNominal;
import si.uni_lj.fri.lrss.machinelearningtoolkit.utils.FeatureNumeric;
import si.uni_lj.fri.lrss.machinelearningtoolkit.utils.Instance;
import si.uni_lj.fri.lrss.machinelearningtoolkit.utils.MLException;
import si.uni_lj.fri.lrss.machinelearningtoolkit.utils.Signature;
import si.uni_lj.fri.lrss.machinelearningtoolkit.utils.Value;


public class MachineLearning extends IntentService{

    final public static String WORK = "work";
    final public static String HOME = "home";

    private MachineLearningManager mlm;
    private LocationDbHelper locationDbHelper;

    public MachineLearning() {
        super("MachineLearning");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        locationDbHelper = new LocationDbHelper(getApplicationContext());
        locationDbHelper.open();
        try {
            Log.e("MachineLearning", "onHandleIntent");
            mlm = MachineLearningManager
                    .getMLManager(this);
            HashMap<String, double[]> centroidMap = learn(getLabeledData());
            updateLocations(centroidMap);
            Log.e("MachineLearning", "configureMl");
            //clasificationTest();
        } catch (Exception e) {
            Log.e("MachineLearning", e.getMessage());
        }
    }

    private void updateLocations(HashMap<String, double[]> centroidMap) {
        JSONArray locationsArray = new JSONArray();
        Log.e("updateLocations", "Starting update");
        for(Map.Entry<String, double[]> location : centroidMap.entrySet()) {
            try {
                JSONObject locationsJson = new JSONObject();
                locationsJson.put("label",location.getKey());
                locationsJson.put("latitude",location.getValue()[0]);
                locationsJson.put("longitude",location.getValue()[1]);
                locationsArray.put(locationsJson);
            } catch (JSONException je) {
                Log.e("updateLocations", je.getMessage());
            }
            Log.e("updateLocations", "updateCentroid()");
            locationDbHelper.updateCentroid(location.getKey(), location.getValue()[0], location.getValue()[1]);
        }
        SharedPreferences prefs = getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Log.e("updateLocations", locationsArray.toString());
        editor.putString("locations", locationsArray.toString());
        editor.commit();

    }

    public HashMap<String, double[]> learn(ArrayList<Instance> instances) {
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
            DensityClustering cls = (DensityClustering) mlm.addClassifier(
                    Constants.TYPE_DENSITY_CLUSTER,
                    signature, config,
                    "location");

            Log.e("MachineLearning", "train!!!");

            cls.train(instances);
            Log.e("MachineLearning", "train completed");

            return cls.getCentroids();

        } catch (Exception e) {
            Log.e("configureML", e.getMessage());
        }
        return null;
    }

    public ArrayList<Instance> getLabeledData() {
        ArrayList<Instance> instanceQ = new ArrayList<Instance>();

        String whereClause = "label IS NOT NULL";
        Cursor cursor = locationDbHelper.getDb().query("location", null, whereClause, null,
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
        } catch (MLException mle) {
            Log.e("MachineLearning", mle.getMessage());
        }
    }

}
