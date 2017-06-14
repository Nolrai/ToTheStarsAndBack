package com.garthskidstuff.shrines.Game;

import android.support.v4.util.Pair;

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
    final static int BUILD_SCOUT_COST = 1;
    final static int BUILD_CARGO_COST = 2;
    final static int BUILD_FIGHTER_COST = 5;
    final static int BUILD_ALTAR_COST = 10;

    private final String imageId;

    private int maxWorkers; // This is really final, but set in an init call AND is the actual int -- not 100th

    private int numWorkerParts;

    private int numUsedWorker;

    private int numAltarParts;

    private int numUsedAltar;

    private int miningRateParts;  // amount of gold generated when 1 worker mines

    private int miningDegradationRateParts;  // mining rate drops each time a worker mines

    private int numGoldParts;

    enum CargoType {
        GOLD,
        WORKER,
        ALTAR
    }

    enum ShipType {
        SCOUT,
        FIGHTER,
        CARGO_EMPTY,
        CARGO_GOLD,
        CARGO_WORKER,
        CARGO_ALTAR,
    }
    // Map "<destinationName>, <ShipType>" --> number of ships
    private Map<String, Map<ShipType, Integer>> movementMap = new HashMap<>();

    private Map<ShipType, Integer> numShipMap = new HashMap<>();

    enum Order {
        MINE,
        BUILD_SCOUT,
        BUILD_CARGO,
        BUILD_FIGHTER,
        BUILD_ALTAR,
        LOAD_CARGO_GOLD,
        LOAD_CARGO_WORKER,
        LOAD_CARGO_ALTAR,
        UNLOAD_CARGO_GOLD,
        UNLOAD_CARGO_WORKER,
        UNLOAD_CARGO_ALTAR,
    };

    public Shrine(String name, String imageId) {
        this.name = name;
        this.imageId = imageId;

        for (ShipType type : ShipType.values()) {
            numShipMap.put(type, 0);
        }
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

    public int getNumShipParts(ShipType type) {
        return numShipMap.get(type);
    }

    public void setNumShipParts(ShipType type, int numParts) {
        numShipMap.put(type, numParts);
    }

    public void addNumShipParts(ShipType type, int numParts) {
        numShipMap.put(type, numShipMap.get(type) + numParts);
    }

    public int getNumShip(ShipType type) {
        return numShipMap.get(type) / PARTS_MULTIPLIER;
    }

    public void setNumShip(ShipType type, int num) {
        numShipMap.put(type, num * PARTS_MULTIPLIER);
    }

    public Map<String, Map<ShipType, Integer>> getMovementMap() {
        return movementMap;
    }

    public void setMovementMap(Map<String, Map<ShipType, Integer>> movementMap) {
        this.movementMap = movementMap;
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

            case BUILD_SCOUT:
                success = payBuildCost(num);
                addNumShipParts(ShipType.SCOUT, numParts / BUILD_SCOUT_COST);
                break;

            case BUILD_CARGO:
                success = payBuildCost(num);
                addNumShipParts(ShipType.CARGO_EMPTY, numParts / BUILD_CARGO_COST);
                break;

            case BUILD_FIGHTER:
                success = payBuildCost(num);
                addNumShipParts(ShipType.FIGHTER, numParts / BUILD_FIGHTER_COST);
                break;

            case BUILD_ALTAR:
                success = payBuildCost(num);
                numAltarParts += (numParts / BUILD_ALTAR_COST);
                break;

            case LOAD_CARGO_GOLD:
            case LOAD_CARGO_WORKER:
            case LOAD_CARGO_ALTAR:
                success = loadUnloadCargo(getCargoType(order), num);
                break;

            case UNLOAD_CARGO_GOLD:
            case UNLOAD_CARGO_WORKER:
            case UNLOAD_CARGO_ALTAR:
                success = loadUnloadCargo(getCargoType(order), -num);
                break;

            default:
//                Log.d(TAG, "Unknown Order: " + order.toString());
                break;
        }
        if (!success) {
            restore(savedState);
        }
    }

    public void doMoveOrder(String destinationName, ShipType type, int num) {
        int numParts = num * PARTS_MULTIPLIER;
        Shrine oldShrine = new Shrine("name", "id");
        oldShrine.setShrine(this);
        addNumShipParts(type, -numParts);
        boolean success = (0 <= getNumShipParts(type));

        addMove(destinationName, type, num);

        if (!success) {
            setShrine(oldShrine);
        }
    }

    void addMove(String destinationName, ShipType type, int num) {
        Map<ShipType, Integer> map = movementMap.get(destinationName);
        if (null == map) {
            map = new HashMap<>();
        }
        Integer curNum = map.get(type);
        map.put(type, (null == curNum) ? num : curNum + num);
        movementMap.put(destinationName, map);
    }

    int getMove(String destinationName, ShipType type) {
        Integer num = 0;
        Map<ShipType, Integer> map = movementMap.get(destinationName);
        if (null != map) {
            num = map.get(type);
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

    CargoType getCargoType(Order order) {
        switch (order) {
            case LOAD_CARGO_GOLD:
            case UNLOAD_CARGO_GOLD:
                return CargoType.GOLD;
            case LOAD_CARGO_WORKER:
            case UNLOAD_CARGO_WORKER:
                return CargoType.GOLD;
            case LOAD_CARGO_ALTAR:
            case UNLOAD_CARGO_ALTAR:
                return CargoType.GOLD;
        }
        return null;
    }

    CargoType getCargoType(ShipType shipType) {
        switch (shipType) {
            case CARGO_GOLD:
                return CargoType.GOLD;
            case CARGO_WORKER:
                return CargoType.GOLD;
            case CARGO_ALTAR:
                return CargoType.GOLD;
        }
        return null;
    }

    void setNumCargo(CargoType cargoType, int num) {
        switch (cargoType) {
            case GOLD:
                setNumGold(num);
                break;
            case WORKER:
                setNumWorker(num);
                break;
            case ALTAR:
                setNumAltar(num);
                break;
        }
    }

    Integer getNumCargo(CargoType cargoType) {
        switch (cargoType) {
            case GOLD:
                return getNumGold();
            case WORKER:
                return getNumWorker();
            case ALTAR:
                return getNumAltar();
        }
        return null;
    }

     ShipType getCargoShipType(CargoType cargoType) {
        switch (cargoType) {
            case GOLD: return ShipType.CARGO_GOLD;
            case WORKER: return ShipType.CARGO_WORKER;
            case ALTAR: return ShipType.CARGO_ALTAR;
        }
        return null;
    }

    private Integer addCargo(CargoType cargoType, int num) {
        int numParts = num * PARTS_MULTIPLIER;
        switch (cargoType) {
            case GOLD:
                return (numGoldParts += numParts);
            case WORKER:
                return (numWorkerParts += numParts);
            case ALTAR:
                return (numAltarParts += numParts);
        }
        return null;
    }

    private boolean loadUnloadCargo(CargoType cargoType, int num) throws NullPointerException { // num < 0 ==> unloading
        int numParts = num * PARTS_MULTIPLIER;
        Integer result = addCargo(cargoType, -num);
        addNumShipParts(ShipType.CARGO_EMPTY, -numParts);
        ShipType shipType = getCargoShipType(cargoType);
        addNumShipParts(shipType, numParts);

        //noinspection ConstantConditions
        return (result >= 0) && (getNumShipParts(shipType) >= 0) && (getNumShipParts(ShipType.CARGO_EMPTY) >= 0);
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
        if (miningRateParts != shrine.miningRateParts) return false;
        if (miningDegradationRateParts != shrine.miningDegradationRateParts) return false;
        if (numGoldParts != shrine.numGoldParts) return false;
        if (!name.equals(shrine.name)) return false;
        if (!imageId.equals(shrine.imageId)) return false;
        if (!movementMap.equals(shrine.movementMap)) return false;
        return numShipMap.equals(shrine.numShipMap);

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
        result = 31 * result + miningRateParts;
        result = 31 * result + miningDegradationRateParts;
        result = 31 * result + numGoldParts;
        result = 31 * result + movementMap.hashCode();
        result = 31 * result + numShipMap.hashCode();
        return result;
    }

    // This does not set name and imageId
    public void setShrine(Shrine other) {
        maxWorkers = other.maxWorkers;
        numWorkerParts = other.numWorkerParts;
        numUsedWorker = other.numUsedWorker;
        numAltarParts = other.numAltarParts;
        numUsedAltar = other.numUsedAltar;
        miningRateParts = other.miningRateParts;
        miningDegradationRateParts = other.miningDegradationRateParts;
        numGoldParts = other.numGoldParts;
        numShipMap = new HashMap<>(other.numShipMap);
        movementMap = new HashMap<>(other.movementMap);
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
        miningRateParts = idx++;
        miningDegradationRateParts = idx++;
        numGoldParts = idx++;
        for (ShipType shipType : ShipType.values()) {
            numShipMap.put(shipType, idx++);
        }
        for (String destinationName : new String[] { "foo", "bar"}) {
            for (ShipType type : ShipType.values()) {
                addMove(destinationName, type, idx++);
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
                ", miningRateParts=" + miningRateParts +
                ", miningDegradationRateParts=" + miningDegradationRateParts +
                ", numGoldParts=" + numGoldParts +
                ", movementMap=" + movementMap +
                ", numShipMap=" + numShipMap +
                '}';
    }
//
//    Map<Pair<String, ShipType>, Integer> getConvertedMovementMap() {
//        Map<Pair<String, ShipType>, Integer> map = new HashMap<>();
//
//        for (String key : movementMap.keySet()) {
//            Pair<String, ShipType> pair = stringToNameAndShipType(key);
//            map.put(pair, movementMap.get(key));
//        }
//
//        return map;
//    }
//
//    private String nameAndShipTypeToString(String name, ShipType type) {
//        return name + "," + type;
//    }
//
//    private Pair<String, ShipType> stringToNameAndShipType(String s) {
//        String[] parts = s.split(",");
//        Pair<String, ShipType> pair = new Pair<>(parts[0], ShipType.valueOf(parts[1]));
//        return pair;
//    }

}
