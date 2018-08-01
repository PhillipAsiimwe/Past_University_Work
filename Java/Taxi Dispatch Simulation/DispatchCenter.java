//Phillip Amanya
import java.util.*;

public class DispatchCenter {
    private HashMap<Integer,Taxi> taxis;
    private HashMap<String,ArrayList<Taxi>> areas;
    public static String[] AREA_NAMES = {"Downtown", "Airport", "North", "South", "East", "West"};

    private int[][]  stats; // You'll need this for the last part of the assignment
    
    
    // Constructor
    public DispatchCenter() {
        // You'll need this for the last part of the assignment
        stats = new int[AREA_NAMES.length][AREA_NAMES.length];
        taxis=new HashMap<>();
        areas=new HashMap<>();
        for (int i=0;i<50;i++) {
            Taxi temp = new Taxi((int) (Math.random() * ((1000 - 100) + 1) + 100));
            addTaxi(temp,AREA_NAMES[(int)(Math.random() * 6)]);
        }

    }
    
    
    // You'll need this for the last part of the assignment
    public int[][]   getStats() { return stats; }
    
    
    // Update the statistics for a taxi going from the pickup location to the dropoff location
    public void updateStats(String pickup, String dropOff) {
        for (int i=0;i<AREA_NAMES.length;i++){
            for (int j=0;j<AREA_NAMES.length;j++){
                if (AREA_NAMES[i].equals(pickup)&& AREA_NAMES[j].equals(dropOff)){
                    stats[i][j]++;
                }
            }
        }
    }
    
    // Determine the travel times from one area to another
    public static int computeTravelTimeFrom(String pickup, String dropOff) {

        int travel[][]={{10,40,20,20,20,20},{40,10,40,40,20,60},{20,40,10,40,20,20},{20,40,40,10,20,20},{20,20,20,20,10,40},{20,60,20,20,40,10}};
        for (int i=0;i<AREA_NAMES.length;i++){
            for (int j=0;j<AREA_NAMES.length;j++){
                if (AREA_NAMES[i].equals(pickup) && AREA_NAMES[j].equals(dropOff)){
                    return travel[i][j];
                }
            }
        }
        return 0;
    }

    // Add a taxi to the hashmaps
    public void addTaxi(Taxi aTaxi, String area) {
        if (!(areas.containsKey(area))){
            areas.put(area,new ArrayList<>());
        }
        taxis.put(aTaxi.getPlateNumber(),aTaxi);
        areas.get(area).add(aTaxi);
    }

    // Return a list of all available taxis within a certain area
    private ArrayList<Taxi> availableTaxisInArea(String s) {
        ArrayList<Taxi> result = new ArrayList<Taxi>();
        for (Taxi x:areas.get(s)){
            if (x.getAvailable()){
                result.add(x);
            }
        }
        return result;
    }

    // Return a list of all busy taxis
    public ArrayList<Taxi> getBusyTaxis() {
        ArrayList<Taxi> result = new ArrayList<Taxi>();
        for (String s: areas.keySet()){
            for (Taxi p:areas.get(s)){
                if (!(p.getAvailable())){
                    result.add(p);
                }
            }
        }
        return result;
    }

    // Find a taxi to satisfy the given request
    public Taxi sendTaxiForRequest(ClientRequest request) {
       String pickup=request.getPickupLocation();
       String dropoff=request.getDropoffLocation();
        Taxi send=null;
        if (!(availableTaxisInArea(pickup).isEmpty())){
            send=availableTaxisInArea(pickup).get(0);
            for (Taxi l:areas.get(pickup)){
                if( l.equals(send)){
                    areas.get(dropoff).add(l);
                    l.setEstimatedTimeToDest(computeTravelTimeFrom(pickup,dropoff));
                    l.setDestination(dropoff);
                    areas.get(pickup).remove(l);
                    l.setAvailable(false);
                    updateStats(pickup,dropoff);
                    return send;
                }
            }
        }else if (availableTaxisInArea(pickup).isEmpty()){
            for (String k:AREA_NAMES){
                if (!(availableTaxisInArea(k).isEmpty())){
                    send=availableTaxisInArea(k).get(0);
                    for (Taxi l:areas.get(k)){
                        if( l.equals(send)){
                            areas.get(dropoff).add(l);
                            areas.get(k).remove(l);
                            l.setAvailable(false);
                            l.setEstimatedTimeToDest(computeTravelTimeFrom(k,pickup)+computeTravelTimeFrom(pickup,dropoff));
                            l.setDestination(dropoff);
                            updateStats(pickup,dropoff);
                            return send;
                        }
                    }
                }
            }
        }
        return null;
    }
    public HashMap<String,ArrayList<Taxi>> getAreas(){return areas;}
}