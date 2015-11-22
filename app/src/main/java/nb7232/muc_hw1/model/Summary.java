package nb7232.muc_hw1.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nejc on 22.11.2015.
 */
public class Summary {

    private HashMap<String, Double> averageRssi;
    private List<AccessPoint> topAccessPoints;

    public Summary() {
        this.averageRssi = new HashMap<>();
        this.topAccessPoints = new ArrayList<>();
    }

    public HashMap<String, Double> getAverageRssi() {
        return averageRssi;
    }

    public void setAverageRssi(HashMap<String, Double> averageRssi) {
        this.averageRssi = averageRssi;
    }

    public List<AccessPoint> getTopAccessPoints() {
        return topAccessPoints;
    }

    public void setTopAccessPoints(List<AccessPoint> topAccessPoints) {
        this.topAccessPoints = topAccessPoints;
    }

    public void addAccessPoint(AccessPoint accessPoint) {
        topAccessPoints.add(accessPoint);
    }

    public void addAverageRssi(String label, Double rssi) {
        averageRssi.put(label, rssi);
    }
}
