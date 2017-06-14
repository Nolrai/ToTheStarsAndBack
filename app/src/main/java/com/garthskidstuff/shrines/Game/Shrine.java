package com.garthskidstuff.shrines.Game;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by garthupshaw1 on 5/10/17.
 * A single node in the World graph
 */

public class Shrine  {
    private static final String TAG = "Shrine";

    private final String name;

    final static int PARTS_MULTIPLIER = 1000;

    // All costs need to divide 1000 evenly.
    final static int BUILD_FIGHTER_COST = 5;
    final static int BUILD_ALTAR_COST = 10;

    private final String imageId;

    private String ownerName = ""; // name of the home world that owns this shrine

    private int maxWorkers; // This is really final, but set in an init call AND is the actual int -- not 100th

    private int numWorkerParts;

    private int numUsedWorker;

    private int numAltarParts;

    private int numUsedAltar;

    private int numFighterParts;

    private int miningRateParts;  // amount of gold generated when 1 worker mines

    private int miningDegradationRateParts;  // mining rate drops each time a worker mines

    private int numGoldParts;

    enum MovableType {
        GOLD(0),
        WORKER(0),
        ALTAR(5),
        FIGHTER(0);

        final int moveCost;

        private MovableType(int moveCost) {
            this.moveCost = moveCost;
        }
    }

    // destination --> (type --> number)
    private Map<String, Map<MovableType, Integer>> departureMap = new HashMap<>();

    // home --> (type --> number)
    private Map<String, Map<MovableType, Integer>> arrivalMap = new HashMap<>();

    enum Order {
        MINE,
        BUILD_FIGHTER,
        BUILD_ALTAR,
    };

    public Shrine(String name, String imageId) {
        this.name = name;
        this.imageId = imageId;
        ownerName = name;
    }

    public void initBasic(int maxPopulation, int miningRateParts, int miningDegradationRateParts) {
        this.maxWorkers = maxPopulation;
        this.miningRateParts = miningRateParts;
        this.miningDegradationRateParts = miningDegradationRateParts;
    }

    public void initHome(int maxPopulation, int miningRate, int miningDegradationRateParts,
                         int numWorkers, int numAlters, int numGold) {
        initBasic(maxPopulation, miningRate, miningDegradationRateParts);
        
        numWorkerParts = numWorkers * PARTS_MULTIPLIER;
        numAltarParts = numAlters * PARTS_MULTIPLIER;
        numGoldParts = numGold * PARTS_MULTIPLIER;
    }

    public String getName() {
        return name;
    }

    public String getImageId() {
        return imageId;
    }

    public int getMaxWorkers() {
        return maxWorkers;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Map<String, Map<MovableType, Integer>> getArrivalMap() {
        return arrivalMap;
    }

    public void setMaxWorkers(int maxWorkers) {
        this.maxWorkers = maxWorkers;
    }

    public int getNumWorkerParts() {
        return numWorkerParts;
    }

    public void setNumWorkerParts(int numWorkerParts) {
        this.numWorkerParts = numWorkerParts;
    }

    public int getNumUsedWorker() {
        return numUsedWorker;
    }

    public void setNumUsedWorker(int numUsedWorker) {
        this.numUsedWorker = numUsedWorker;
    }

    public int getNumAltarParts() {
        return numAltarParts;
    }

    public void setNumAltarParts(int numAltarParts) {
        this.numAltarParts = numAltarParts;
    }

    public int getNumUsedAltar() {
        return numUsedAltar;
    }

    public void setNumUsedAltar(int numUsedAltar) {
        this.numUsedAltar = numUsedAltar;
    }

    public int getMiningRateParts() {
        return miningRateParts;
    }

    public void setMiningRateParts(int miningRateParts) {
        this.miningRateParts = miningRateParts;
    }

    public int getMiningDegradationRateParts() {
        return miningDegradationRateParts;
    }

    public void setMiningDegradationRateParts(int miningDegradationRateParts) {
        this.miningDegradationRateParts = miningDegradationRateParts;
    }

    public int getNumGoldParts() {
        return numGoldParts;
    }

    public void setNumGoldParts(int numGoldParts) {
        this.numGoldParts = numGoldParts;
    }

    public int getNumFighterParts() {
        return numFighterParts;
    }

    public void setNumFighterParts(int numFighterParts) {
        this.numFighterParts = numFighterParts;
    }

    public Map<String, Map<MovableType, Integer>> getDepartureMap() {
        return departureMap;
    }

    // The following are convenience functions to get/set whole integer values
    public int getNumGold() {
        return numGoldParts / PARTS_MULTIPLIER;
    }

    public void setNumGold(int num) {
        numGoldParts = num * PARTS_MULTIPLIER;
    }

    public int getNumWorker() {
        return numWorkerParts / PARTS_MULTIPLIER;
    }

    public void setNumWorker(int num) {
        numWorkerParts = num * PARTS_MULTIPLIER;
    }

    public int getNumAltar() {
        return numAltarParts / PARTS_MULTIPLIER;
    }

    public void setNumAltar(int num) {
        numAltarParts = num * PARTS_MULTIPLIER;
    }

    public int getNumFighter() {
        return numFighterParts / PARTS_MULTIPLIER;
    }

    public void setNumFighter(int num) {
        numFighterParts = num * PARTS_MULTIPLIER;
    }

    public void setMovableType(MovableType type, int num) {
        switch (type) {
            case WORKER:
                setNumWorker(num);
                break;
            case GOLD:
                setNumGold(num);
                break;
            case ALTAR:
                setNumAltar(num);
                break;
            case FIGHTER:
                setNumFighter(num);
                break;
        }
    }

    public Integer getMovableType(MovableType type) {
        switch (type) {
            case WORKER:
                return getNumWorker();
            case GOLD:
                return getNumGold();
            case ALTAR:
                return getNumAltar();
            case FIGHTER:
                return getNumFighter();
        }
        return null;
    }

    String makeSavedState() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    void restore(String savedState) {
        Gson gson = new Gson();
        Shrine shrine = gson.fromJson(savedState, Shrine.class);
        setShrine(shrine);
    }

    void doOrder(Order order, int num) {
        int numParts = num * PARTS_MULTIPLIER;
        String savedState = makeSavedState();
        boolean success = true;
        switch (order) {
            case MINE:
                success = useWorkers(num);
                numGoldParts += num * miningRateParts;
                miningRateParts -= num * miningDegradationRateParts;
                break;

            case BUILD_FIGHTER:
                success = payBuildCost(num);
                numFighterParts += (numParts / BUILD_FIGHTER_COST);
                break;

            case BUILD_ALTAR:
                success = payBuildCost(num);
                numAltarParts += (numParts / BUILD_ALTAR_COST);
                break;

            default:
//                Log.d(TAG, "Unknown Order: " + order.toString());
                break;
        }
        if (!success) {
            restore(savedState);
        }
    }

    public void doMoveOrder(String destinationName, MovableType type, int num) {
        Shrine oldShrine = new Shrine("name", "id");
        oldShrine.setShrine(this);

        addDeparture(destinationName, type, num);

        if (!useGold(type.moveCost) || !useMovable(type, num)) {
            setShrine(oldShrine);
        }
    }

    boolean useMovable(MovableType type, int num) {
        int numParts = num * PARTS_MULTIPLIER;
        switch(type) {
            case WORKER:
                numWorkerParts -= numParts;
                return (0 <= numWorkerParts);
            case GOLD:
                numGoldParts -= numParts;
                return (0 <= numGoldParts);
            case ALTAR:
                numAltarParts -= numParts;
                return (0 <= numAltarParts);
            case FIGHTER:
                numFighterParts -= numParts;
                return (0 <= numFighterParts);
        }
        return false;
    }

    void addDeparture(String destinationName, MovableType type, int num) {
        addToMap(departureMap, destinationName, type, num);
    }

    int getDeparture(String destinationName, MovableType type) {
        return getFromMap(departureMap, destinationName, type);
    }

    void addArrival(String homeName, MovableType type, int num) {
        addToMap(arrivalMap, homeName, type, num);
    }

    int getArrival(String destinationName, MovableType type) {
        return getFromMap(arrivalMap, destinationName, type);
    }

    private void addToMap(Map<String, Map<MovableType, Integer>> map, String destinationName, MovableType type, int num) {
        Map<MovableType, Integer> subMap = map.get(destinationName);
        if (null == subMap) {
            subMap = new HashMap<>();
        }
        Integer curNum = subMap.get(type);
        subMap.put(type, (null == curNum) ? num : curNum + num);
        map.put(destinationName, subMap);
    }

    private int getFromMap(Map<String, Map<MovableType, Integer>> map, String id, MovableType type) {
        Integer num = 0;
        Map<MovableType, Integer> subMap = map.get(id);
        if (null != subMap) {
            num = subMap.get(type);
        }
        return (null == num) ? 0 : num;
    }


    private boolean payBuildCost(int num) {
        return useWorkers(num) && useGold(num) && useAltars(num);
    }

    private boolean useWorkers(int num) {
        int numParts = num * PARTS_MULTIPLIER;
        numUsedWorker += num;
        numWorkerParts -= numParts;
        return (numWorkerParts >= 0);
    }

    private boolean useGold(int num) {
        int numParts = num * PARTS_MULTIPLIER;
        numGoldParts -= numParts;
        return (numGoldParts >= 0);
    }

    private boolean useAltars(int num) {
        int numParts = num * PARTS_MULTIPLIER;
        numUsedAltar += num;
        numAltarParts -= numParts;
        return (numAltarParts >= 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Shrine shrine = (Shrine) o;

        if (maxWorkers != shrine.maxWorkers) return false;
        if (numWorkerParts != shrine.numWorkerParts) return false;
        if (numUsedWorker != shrine.numUsedWorker) return false;
        if (numAltarParts != shrine.numAltarParts) return false;
        if (numUsedAltar != shrine.numUsedAltar) return false;
        if (numFighterParts != shrine.numFighterParts) return false;
        if (miningRateParts != shrine.miningRateParts) return false;
        if (miningDegradationRateParts != shrine.miningDegradationRateParts) return false;
        if (numGoldParts != shrine.numGoldParts) return false;
        if (!name.equals(shrine.name)) return false;
        if (!imageId.equals(shrine.imageId)) return false;
        if (!departureMap.equals(shrine.departureMap)) return false;
        return arrivalMap.equals(shrine.arrivalMap);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + imageId.hashCode();
        result = 31 * result + maxWorkers;
        result = 31 * result + numWorkerParts;
        result = 31 * result + numUsedWorker;
        result = 31 * result + numAltarParts;
        result = 31 * result + numUsedAltar;
        result = 31 * result + numFighterParts;
        result = 31 * result + miningRateParts;
        result = 31 * result + miningDegradationRateParts;
        result = 31 * result + numGoldParts;
        result = 31 * result + departureMap.hashCode();
        result = 31 * result + arrivalMap.hashCode();
        return result;
    }

    // This does not set name and imageId
    public void setShrine(Shrine other) {
        maxWorkers = other.maxWorkers;
        numWorkerParts = other.numWorkerParts;
        numUsedWorker = other.numUsedWorker;
        numAltarParts = other.numAltarParts;
        numUsedAltar = other.numUsedAltar;
        numFighterParts = other.numFighterParts;
        miningRateParts = other.miningRateParts;
        miningDegradationRateParts = other.miningDegradationRateParts;
        numGoldParts = other.numGoldParts;
        departureMap = new HashMap<>(other.departureMap);
        arrivalMap = new HashMap<>(other.arrivalMap);
    }

    /**
     * Set all of a shrine's values -- for testing only
     */
    void setAllValues() {
        int idx = 1;
        maxWorkers = idx++;
        numWorkerParts = idx++;
        numUsedWorker = idx++;
        numAltarParts = idx++;
        numUsedAltar = idx++;
        numFighterParts = idx++;
        miningRateParts = idx++;
        miningDegradationRateParts = idx++;
        numGoldParts = idx++;

        departureMap.clear();
        for (String destinationName : new String[] { "foo", "bar"}) {
            for (MovableType type : MovableType.values()) {
                addDeparture(destinationName, type, idx++);
            }
        }

        arrivalMap.clear();
        for (String homeId : new String[] { "foo", "bar"}) {
            for (MovableType type : MovableType.values()) {
                addArrival(homeId, type, idx++);
            }
        }
    }

    @Override
    public String toString() {
        return "Shrine{" +
                "name='" + name + '\'' +
                ", imageId='" + imageId + '\'' +
                ", maxWorkers=" + maxWorkers +
                ", numWorkerParts=" + numWorkerParts +
                ", numUsedWorker=" + numUsedWorker +
                ", numAltarParts=" + numAltarParts +
                ", numUsedAltar=" + numUsedAltar +
                ", numFighterParts=" + numFighterParts +
                ", miningRateParts=" + miningRateParts +
                ", miningDegradationRateParts=" + miningDegradationRateParts +
                ", numGoldParts=" + numGoldParts +
                ", departureMap=" + departureMap +
                ", arrivalMap=" + arrivalMap +
                '}';
    }

}
