package com.garthskidstuff.shrines.Game;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by garthupshaw1 on 5/10/17.
 * A single node in the World graph
 */

class Shrine  {
    static final String TAG = "Shrine";

    static final String ORDER = "Order: ";

    static final String NUM = "Num: ";

    private final int id;

    private final String displayName;

    final static int PARTS_MULTIPLIER = 100 * 1000;

    // All costs need to divide PARTS_MULTIPLIER evenly.
    final static int BUILD_FIGHTER_COST = 5;
    final static int BUILD_ALTAR_COST = 10;

    private final String imageId;

    private int ownerId; // id of the home world that owns this shrine

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

        MovableType(int buildCost, int moveCost, int fight) {
            this.buildCost = buildCost;
            this.moveCost = moveCost;
            this.fight = fight;
        }
    }

    // destination --> (type --> number)
    private Map<Integer, Map<MovableType, Integer>> departureMap = new HashMap<>();

    // home --> (type --> number)
    private Map<Integer, Map<MovableType, Integer>> arrivalMap = new HashMap<>();

    enum Order {
        MINE,
        BUILD_FIGHTER,
        BUILD_ALTAR,
    }

    Shrine(int id, String displayName, String imageId) {
        this.id = id;
        this.displayName = displayName;
        this.imageId = imageId;
        ownerId = id;
    }

    void initBasic(int maxPopulation, int miningRateParts, int miningDegradationRateParts, int workerRateParts) {
        this.maxWorkers = maxPopulation;
        this.miningRateParts = miningRateParts;
        this.miningDegradationRateParts = miningDegradationRateParts;
        this.workerRateParts = workerRateParts;
    }

    void initHome(int maxPopulation, int miningRate, int miningDegradationRateParts,
                         int workerRateParts, int numWorkers, int numAlters, int numGold) {
        initBasic(maxPopulation, miningRate, miningDegradationRateParts, workerRateParts);
        
        numWorkerParts = numWorkers * PARTS_MULTIPLIER;
        numAltarParts = numAlters * PARTS_MULTIPLIER;
        numGoldParts = numGold * PARTS_MULTIPLIER;
    }

    int getId() {
        return id;
    }

    @SuppressWarnings("WeakerAccess")
    public String getDisplayName() {
        return displayName;
    }

    @SuppressWarnings("WeakerAccess")
    public String getImageId() {
        return imageId;
    }

    @SuppressWarnings("WeakerAccess")
    public int getOwnerId() {return ownerId;}

    @SuppressWarnings("WeakerAccess")
    public int getMaxWorkers() {
        return maxWorkers;
    }

    private void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    @SuppressWarnings("WeakerAccess")
    public int getNumWorkerParts() {
        return numWorkerParts;
    }

    private void setNumWorkerParts(int numWorkerParts) {
        this.numWorkerParts = numWorkerParts;
    }

    @SuppressWarnings("WeakerAccess")
    public int getNumUsedWorker() {
        return numUsedWorker;
    }

    @SuppressWarnings("WeakerAccess")
    public void setNumUsedWorker(int numUsedWorker) {
        this.numUsedWorker = numUsedWorker;
    }

    @SuppressWarnings("WeakerAccess")
    public int getNumAltarParts() {
        return numAltarParts;
    }

    private void setNumAltarParts(int numAltarParts) {
        this.numAltarParts = numAltarParts;
    }

    @SuppressWarnings("WeakerAccess")
    public int getNumUsedAltar() {
        return numUsedAltar;
    }

    @SuppressWarnings("WeakerAccess")
    public void setNumUsedAltar(int numUsedAltar) {
        this.numUsedAltar = numUsedAltar;
    }

    @SuppressWarnings("WeakerAccess")
    public int getMiningRateParts() {
        return miningRateParts;
    }

    @SuppressWarnings("WeakerAccess")
    public int getMiningDegradationRateParts() {
        return miningDegradationRateParts;
    }

    @SuppressWarnings("WeakerAccess")
    public int getNumGoldParts() {
        return numGoldParts;
    }

    private void setNumGoldParts(int numGoldParts) {
        this.numGoldParts = numGoldParts;
    }

    @SuppressWarnings("WeakerAccess")
    public int getNumFighterParts() {
        return numFighterParts;
    }

    private void setNumFighterParts(int numFighterParts) {
        this.numFighterParts = numFighterParts;
    }

    @SuppressWarnings("WeakerAccess")
    public Map<Integer, Map<MovableType, Integer>> getDepartureMapCopy() {
        return copyMap(departureMap);
    }

    Map<Integer, Map<MovableType, Integer>> getArrivalMapCopy () {
        return copyMap(arrivalMap);
    }

    private Map<Integer, Map<MovableType, Integer>> copyMap(Map<Integer, Map<MovableType, Integer>>  map) {
        Map<Integer, Map<MovableType, Integer>> copyMap = new HashMap<>();

        for (Integer key : map.keySet()) {
            Map<MovableType, Integer> copySubMap = new HashMap<>(map.get(key));
            copyMap.put(key, copySubMap);
        }

        return copyMap;
    }

    void clearDepartureMap() {
        departureMap.clear();
    }

    // The following are convenience functions to get/set whole integer values
    @SuppressWarnings("WeakerAccess")
    public int getNumGold() {
        return numGoldParts / PARTS_MULTIPLIER;
    }

    void setNumGold(int num) {
        numGoldParts = num * PARTS_MULTIPLIER;
    }

    @SuppressWarnings("WeakerAccess")
    public int getNumWorker() {
        return numWorkerParts / PARTS_MULTIPLIER;
    }

    void setNumWorker(int num) {
        numWorkerParts = num * PARTS_MULTIPLIER;
    }

    @SuppressWarnings("WeakerAccess")
    public int getNumAltar() {
        return numAltarParts / PARTS_MULTIPLIER;
    }

    void setNumAltar(int num) {
        numAltarParts = num * PARTS_MULTIPLIER;
    }

    @SuppressWarnings({"WeakerAccess", "unused"})
    public int getNumFighter() {
        return numFighterParts / PARTS_MULTIPLIER;
    }

    void setMovableType(MovableType type, int num) {
        setMovableTypeParts(type, num * PARTS_MULTIPLIER);
    }

    private void setMovableTypeParts(MovableType type, int numParts) {
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

    Integer getMovableType(MovableType type) {
        //noinspection ConstantConditions
        return getMovableTypeParts(type) / PARTS_MULTIPLIER;
    }

    private Integer getMovableTypeParts(MovableType type) {
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

    private void addMovableType(MovableType type, int num) {
        //noinspection ConstantConditions
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

    boolean doOrder(Order order, int num) {
        Logger.i(TAG, ORDER + order + " " + NUM + num);
        int numParts = num * PARTS_MULTIPLIER;
        Shrine oldShrine = cloneShrine(-1);

        boolean success = true;
        switch (order) {
            case MINE:
                success = useWorkers(num);
                int newMiningRateParts = miningRateParts - num * miningDegradationRateParts;
                numGoldParts += (num * (miningRateParts + newMiningRateParts)) / 2;
                miningRateParts = newMiningRateParts;
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
            setShrine(oldShrine);
        }

        return success;
    }

    boolean doMoveOrder(int destinationId, MovableType type, int num) {
        Shrine oldShrine = new Shrine(-1, "name", "image");
        oldShrine.setShrine(this);

        addDeparture(destinationId, type, num);

        boolean success = useGold(type.moveCost) && useMovable(type, num);
        if (!success) {
            setShrine(oldShrine);
        }
        return success;
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

    void addDeparture(int destinationId, MovableType type, int num) {
        addToMap(departureMap, destinationId, type, num);
    }

    int getDeparture(int destinationId, MovableType type) {
        return getFromMap(departureMap, destinationId, type);
    }

    void addArrival(int homeId, MovableType type, int num) {
        addToMap(arrivalMap, homeId, type, num);
    }

    private void addToMap(Map<Integer, Map<MovableType, Integer>> map, int destinationId, MovableType type, int num) {
        if (0 != num) {
            Map<MovableType, Integer> subMap = map.get(destinationId);
            if (null == subMap) {
                subMap = new HashMap<>();
            }
            Integer curNum = subMap.get(type);
            subMap.put(type, (null == curNum) ? num : curNum + num);
            map.put(destinationId, subMap);
        }
    }

    private int getFromMap(Map<Integer, Map<MovableType, Integer>> map, int id, MovableType type) {
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
            addArrival(id, type, getMovableType(type));
            //noinspection ConstantConditions
            int fractionalPart = getMovableTypeParts(type) % PARTS_MULTIPLIER;
            setMovableTypeParts(type, fractionalPart);
        }
    }

    void fight(Roller roller) {

        // Do fights
        Map<Integer, Map<MovableType, Integer>> copyMap = getArrivalMapCopy();
        while (2 <= copyMap.keySet().size()) {
            // Square the sum of the fight values for each player
            Map<Integer, Integer> sumMap = computeSquareSumMap(copyMap);

            // Given the above, pick a winner for this round (null ==> no winner)
            Integer winnerId = chooseWeightedKey(sumMap, roller);
            if (null != winnerId) {
                // damage all other players
                for (Integer key : copyMap.keySet()) {
                    if (!Utils.equals(winnerId, key)) {
                        Map<MovableType, Integer> subMap = copyMap.get(key);
                        MovableType hit = chooseWeightedKey(subMap, roller);
                        subMap.put(hit, (subMap.get(hit) - 1));
                    }
                }
            }

            // Remove any player who is completely wiped out
            Set<Integer> removeMe = new HashSet<>();
            for (Integer key : copyMap.keySet()) {
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
            for (Integer key : removeMe) {
                copyMap.remove(key);
            }
        }

        // move everything with non-zero fight (e.g. FIGHTER) back to shrine
        if (1 == copyMap.keySet().size()) {
            for (Integer key : copyMap.keySet()) {
                setOwnerId(key);
                Map<MovableType, Integer> subMap = copyMap.get(key);
                for (MovableType type : subMap.keySet()) {
                    if (0 < type.fight) {
                        addMovableType(type, subMap.get(type));
                    }
                }
            }
        }

        // move everything with zero fight (e.g. GOLD) back to shrine
        for (Integer key : arrivalMap.keySet()) {
            setOwnerId(key);
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

    private Map<Integer, Integer> computeSquareSumMap(Map<Integer, Map<MovableType, Integer>> map) {
        Map<Integer, Integer> sumMap = new HashMap<>();
        for (Integer key : map.keySet()) {
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

    Shrine cloneShrine(int newID) {
        Shrine s = new Shrine(newID, getDisplayName(), getImageId());
        s.setShrine(this);
        return s;
    }

    String toCLIString() {
        return "Shrine " + id + "\n    " +
                " ownerId=" + ownerId +
                " maxWorkers=" + maxWorkers +
                " workers=" + numWorkerParts +
                " numUsedWorker=" + numUsedWorker +
                " numAltarParts=" + numAltarParts +
                " numUsedAltar=" + numUsedAltar + "\n    " +
                " numFighterParts=" + numFighterParts +
                " miningRateParts=" + miningRateParts +
                " numGoldParts=" + numGoldParts +
                " workerRateParts=" + workerRateParts +
                " departureMap=" + departureMap +
                " arrivalMap=" + arrivalMap;
    }

    @Override
    public String toString() {
        return "Shrine{" +
                "id='" + id + '\'' +
                ", imageId='" + imageId + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", maxWorkers=" + maxWorkers +
                ", numWorkerParts=" + numWorkerParts +
                ", numUsedWorker=" + numUsedWorker +
                ", numAltarParts=" + numAltarParts +
                ", numUsedAltar=" + numUsedAltar +
                ", numFighterParts=" + numFighterParts +
                ", miningRateParts=" + miningRateParts +
                ", miningDegradationRateParts=" + miningDegradationRateParts +
                ", numGoldParts=" + numGoldParts +
                ", workerRateParts=" + workerRateParts +
                ", departureMap=" + departureMap +
                ", arrivalMap=" + arrivalMap +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Shrine shrine = (Shrine) o;

        if (id != shrine.id) return false;
        if (ownerId != shrine.ownerId) return false;
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
        if (!displayName.equals(shrine.displayName)) return false;
        if (!imageId.equals(shrine.imageId)) return false;
        //noinspection SimplifiableIfStatement
        if (!departureMap.equals(shrine.departureMap)) return false;
        return arrivalMap.equals(shrine.arrivalMap);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + displayName.hashCode();
        result = 31 * result + imageId.hashCode();
        result = 31 * result + ownerId;
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

    // This does not set id, imageId or displayName
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
        for (Integer destinationId : new Integer[] { idx++, idx++}) {
            for (MovableType type : MovableType.values()) {
                addDeparture(destinationId, type, idx++);
            }
        }

        arrivalMap.clear();
        for (Integer homeId : new Integer[] { idx++, idx++}) {
            for (MovableType type : MovableType.values()) {
                addArrival(homeId, type, idx++);
            }
        }
    }

}
