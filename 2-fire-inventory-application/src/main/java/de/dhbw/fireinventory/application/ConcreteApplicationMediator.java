package de.dhbw.fireinventory.application;

import de.dhbw.fireinventory.application.mediator.colleague.Colleague;
import de.dhbw.fireinventory.application.mediator.colleague.HasLinkedException;
import de.dhbw.fireinventory.domain.condition.Condition;
import de.dhbw.fireinventory.domain.item.Item;
import de.dhbw.fireinventory.domain.location.Location;
import de.dhbw.fireinventory.domain.vehicle.Vehicle;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ConcreteApplicationMediator implements Mediator {

    private final Map<Integer, ArrayList<Colleague>> colleagues =
            new TreeMap<Integer,ArrayList<Colleague>>();

    public void addColleague(Colleague colleague, int priority) {
        deleteColleague(colleague);
        if(!colleagues.containsKey(priority)){
            colleagues.put(priority,new ArrayList<Colleague>());
        }
        colleagues.get(priority).add(colleague);
    }

    public boolean deleteColleague(Colleague colleague){
        for(Integer x : colleagues.keySet()){
            for(int i=0;i<colleagues.get(x).size();i++){
                if(colleagues.get(x).get(i) == colleague){
                    colleagues.get(x).remove(i);
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public void onDeleteLocation(Location location, Colleague colleague) throws HasLinkedException {
        TreeSet<Integer> t = new TreeSet<Integer>();
        t.addAll(colleagues.keySet());
        Iterator<Integer> it = t.descendingIterator();
        while(it.hasNext()){
            int x = it.next();
            ArrayList<Colleague> l = colleagues.get(x);
            for(Colleague c : l){
                if (!c.equals(colleague)) c.onDeleteLocation(location);
            }
        }
    }

    @Override
    public void onSaveVehicle(Item item, Condition condition, Colleague colleague) {
        TreeSet<Integer> t = new TreeSet<Integer>();
        t.addAll(colleagues.keySet());
        Iterator<Integer> it = t.descendingIterator();
        while(it.hasNext()){
            int x = it.next();
            ArrayList<Colleague> l = colleagues.get(x);
            for(Colleague c : l){
                if (!c.equals(colleague)) c.onSaveVehicle(item, condition);
            }
        }
    }

    @Override
    public void onDeleteVehicle(Item item, Colleague colleague) throws HasLinkedException {
        TreeSet<Integer> t = new TreeSet<Integer>();
        t.addAll(colleagues.keySet());
        Iterator<Integer> it = t.iterator();
        while(it.hasNext()){
            int x = it.next();
            ArrayList<Colleague> l = colleagues.get(x);
            for(Colleague c : l){
                if (!c.equals(colleague)) c.onDeleteVehicle(item);
            }
        }
    }
}
