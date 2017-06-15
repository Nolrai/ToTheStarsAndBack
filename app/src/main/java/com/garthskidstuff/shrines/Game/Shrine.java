package com.garthskidstuff.shrines.Game;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Created by garthupshaw1 on 5/10/17.
 * A single node in the World graph
 */

public class Shrine  {
    private static final String TAG = "Shrine";

    private final String name;

    final static int PARTS_MULTIPLIER = 100 * 1000;

    // All costs need to divide PARTS_MULTIPLIER evenly.
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

    private int miningRateParts;  // amount of gold (parts) generated when 1 (whole) worker mines

    private int miningDegradationRateParts;  // mining rate drops each time a worker mines

    private int numGoldParts;

    private int workerRateParts; // amount worker (parts) increases for each whole worker

    // All build costs need to divide PARTS_MULTIPLIER evenly.
    enum MovableType {
        GOLD(-1, 0, 0),
        WORKER(-1, 0, 1),
        ALTAR(10, 5, 1),
        FIGHTER(5, 0, 5);

        final int buildCost;
        final int moveCost;
        final int fight;

        private MovableType(int buildCost, int moveCost, int fight) {
            this.buildCost = buildCost;
            this.moveCost = moveCost;
            this.fight = fight;
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

    private Shrine(String name, Shrine shrine_) {
        this.name = name;
        this.imageId = shrine_.imageId;
        ownerName = name;
        setShrine(shrine_);
    }

    public Shrine(String name, String imageId) {
        this.name = name;
        this.imageId = imageId;
        ownerName = name;
    }

    public void initBasic(int maxPopulation, int miningRateParts, int miningDegradationRateParts, int workerRateParts) {
        this.maxWorkers = maxPopulation;
        this.miningRateParts = miningRateParts;
        this.miningDegradationRateParts = miningDegradationRateParts;
        this.workerRateParts = workerRateParts;
    }

    public void initHome(int maxPopulation, int miningRate, int miningDegradationRateParts,
                         int workerRateParts, int numWorkers, int numAlters, int numGold) {
        initBasic(maxPopulation, miningRate, miningDegradationRateParts, workerRateParts);
        
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

    public int getMiningDegradationRateParts() {
        return miningDegradationRateParts;
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

    public Map<String, Map<MovableType, Integer>> getDepartureMapCopy() {
        return copyMap(departureMap);
    }

    private Map<String, Map<MovableType, Integer>> copyMap(Map<String, Map<MovableType, Integer>>  map) {
        Map<String, Map<MovableType, Integer>> copyMap = new HashMap<>();

        for (String key : map.keySet()) {
            Map<MovableType, Integer> copySubMap = new HashMap<>(map.get(key));
            copyMap.put(key, copySubMap);
        }

        return copyMap;
    }

    public void clearDepartureMap() {
        departureMap.clear();
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
        setMovableTypeParts(type, num * PARTS_MULTIPLIER);
    }

    public void setMovableTypeParts(MovableType type, int numParts) {
        switch (type) {
            case WORKER:
                setNumWorkerParts(numParts);
                break;
            case GOLD:
                setNumGoldParts(numParts);
                break;
            case ALTAR:
                setNumAltarParts(numParts);
                break;
            case FIGHTER:
                setNumFighterParts(numParts);
                break;
        }
    }

    public Integer getMovableType(MovableType type) {
        return getMovableTypeParts(type) / PARTS_MULTIPLIER;
    }

    public Integer getMovableTypeParts(MovableType type) {
        switch (type) {
            case WORKER:
                return getNumWorkerParts();
            case GOLD:
                return getNumGoldParts();
            case ALTAR:
                return getNumAltarParts();
            case FIGHTER:
                return getNumFighterParts();
        }
        return null;
    }

    public void addMovableType(MovableType type, int num) {
        int existingParts = getMovableTypeParts(type);
        setMovableTypeParts(type, existingParts + num * PARTS_MULTIPLIER);
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
                for (int i = 0; i < num; i++) {
                    numGoldParts += miningRateParts - miningDegradationRateParts/2;
                    miningRateParts -= miningDegradationRateParts;
                }
                break;

            case BUILD_FIGHTER:
                success = payBuildCost(num);
                numFighterParts += (numParts / MovableType.FIGHTER.buildCost);
                break;

            case BUILD_ALTAR:
                success = payBuildCost(num);
                numAltarParts += (numParts / MovableType.ALTAR.buildCost);
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

    private boolean useMovable(MovableType type, int num) {
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

    Map<String, Map<MovableType, Integer>> getArrivalMapCopy() {
        return copyMap(departureMap);
    }
    private void addToMap(Map<String, Map<MovableType, Integer>> map, String destinationName, MovableType type, int num) {
        if (0 != num) {
            Map<MovableType, Integer> subMap = map.get(destinationName);
            if (null == subMap) {
                subMap = new HashMap<>();
            }
            Integer curNum = subMap.get(type);
            subMap.put(type, (null == curNum) ? num : curNum + num);
            map.put(destinationName, subMap);
        }
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

    void moveAllToArrivalMap() {
        for (MovableType type : MovableType.values()) {
            addArrival(name, type, getMovableType(type));
            int fractionalPart = getMovableTypeParts(type) % PARTS_MULTIPLIER;
            setMovableTypeParts(type, fractionalPart);
        }
    }

    void fight(Roller roller) {

        // Do fights
        Map<String, Map<MovableType, Integer>> copyMap = getArrivalMapCopy();
        while (2 <= copyMap.keySet().size()) {
            // Square the sum of the fight values for each player
            Map<String, Integer> sumMap = computeSquareSumMap(copyMap);

            // Given the above, pick a winner for this round (null ==> no winner)
            String winner = chooseWeightedKey(sumMap, roller);
            if (null != winner) {
                // damage all other players
                for (String key : copyMap.keySet()) {
                    if (!Utils.equals(winner, key)) {
                        Map<MovableType, Integer> subMap = copyMap.get(key);
                        MovableType hit = chooseWeightedKey(subMap, roller);
                        subMap.put(hit, (subMap.get(hit) - 1));
                    }
                }
            }

            // Remove any player who is completely wiped out
            Set<String> removeMe = new HashSet<>();
            for (String key : copyMap.keySet()) {
                Map<MovableType, Integer> subMap = copyMap.get(key);
                boolean remove = true;
                for (MovableType type : subMap.keySet()) {
                    if ((0 < type.fight) && (0 < subMap.get(type))) { // Gold doesn't count
                        remove = false;
                        break;
                    }
                }
                if (remove) {
                    removeMe.add(key);
                }
            }
            for (String key : removeMe) {
                copyMap.remove(key);
            }
        }

        // move everything with non-zero fight (e.g. FIGHTER) back to shrine
        if (1 == copyMap.keySet().size()) {
            for (String key : copyMap.keySet()) {
                ownerName = key;
                Map<MovableType, Integer> subMap = copyMap.get(key);
                for (MovableType type : subMap.keySet()) {
                    if (0 < type.fight) {
                        addMovableType(type, subMap.get(type));
                    }
                }
            }
        }

        // move everything with zero fight (e.g. GOLD) back to shrine
        for (String key : arrivalMap.keySet()) {
            ownerName = key;
            Map<MovableType, Integer> subMap = arrivalMap.get(key);
            for (MovableType type : subMap.keySet()) {
                if (0 == type.fight) {
                    addMovableType(type, subMap.get(type));
                }
            }
        }

        arrivalMap.clear();
    }

    private <T> T chooseWeightedKey(Map<T,Integer> map, Roller roller) {
        T winner = null;
        int totalSum = 0;
        for (int num : map.values()) {
            totalSum += num;
        }
        if (0 < totalSum) {
            int hit = roller.roll(0, totalSum - 1);
            for (T key : map.keySet()) {
                hit -= map.get(key);
                if (hit < 0) {
                    winner = key;
                    break;
                }
                hit -= map.get(key);
            }
        }
        return winner;
    }

    private Map<String, Integer> computeSquareSumMap(Map<String, Map<MovableType, Integer>> map) {
        Map<String, Integer> sumMap = new HashMap<>();
        for (String key : map.keySet()) {
            Map<MovableType, Integer> subMap = map.get(key);
            int sum = 0;
            for (MovableType type : subMap.keySet()) {
                sum += subMap.get(type) * type.fight;
            }
            sumMap.put(key, sum * sum);
        }
        return sumMap;
    }

    void endTurn() {
        // Unused workers automatically mine
        doOrder(Order.MINE, getNumWorker());

        // reset used workers and Alters
        addMovableType(MovableType.WORKER, getNumUsedWorker());
        setNumUsedWorker(0);
        addMovableType(MovableType.ALTAR, getNumUsedAltar());
        setNumUsedAltar(0);

        // workers eat gold and STARVE if not enough gold
        addMovableType(Shrine.MovableType.GOLD, -getNumWorker());
        if (0 > getNumGold()) {
            addMovableType(Shrine.MovableType.WORKER, getNumGold());
            setNumGold(0);
        }

        // Workers reproduce (but not more than max workers)
        setNumWorkerParts(getNumWorkerParts() + getNumWorker() * workerRateParts);
        if (getNumWorkerParts() > maxWorkers * PARTS_MULTIPLIER) {
            setNumWorker(maxWorkers);
        }
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
        if (workerRateParts != shrine.workerRateParts) return false;
        if (!name.equals(shrine.name)) return false;
        if (!imageId.equals(shrine.imageId)) return false;
        if (!ownerName.equals(shrine.ownerName)) return false;
        //noinspection SimplifiableIfStatement
        if (!departureMap.equals(shrine.departureMap)) return false;
        return arrivalMap.equals(shrine.arrivalMap);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + imageId.hashCode();
        result = 31 * result + ownerName.hashCode();
        result = 31 * result + maxWorkers;
        result = 31 * result + numWorkerParts;
        result = 31 * result + numUsedWorker;
        result = 31 * result + numAltarParts;
        result = 31 * result + numUsedAltar;
        result = 31 * result + numFighterParts;
        result = 31 * result + miningRateParts;
        result = 31 * result + miningDegradationRateParts;
        result = 31 * result + numGoldParts;
        result = 31 * result + workerRateParts;
        result = 31 * result + departureMap.hashCode();
        result = 31 * result + arrivalMap.hashCode();
        return result;
    }

    // This does not set name and imageId
    private void setShrine(Shrine other) {
        maxWorkers = other.maxWorkers;
        numWorkerParts = other.numWorkerParts;
        numUsedWorker = other.numUsedWorker;
        numAltarParts = other.numAltarParts;
        numUsedAltar = other.numUsedAltar;
        numFighterParts = other.numFighterParts;
        miningRateParts = other.miningRateParts;
        miningDegradationRateParts = other.miningDegradationRateParts;
        numGoldParts = other.numGoldParts;
        workerRateParts = other.workerRateParts;
        departureMap = new HashMap<>(other.departureMap);
        arrivalMap = new HashMap<>(other.arrivalMap);
    }

    Shrine makeCopy(String newName) {
        return new Shrine(newName, this);
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
        workerRateParts = idx++;

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
