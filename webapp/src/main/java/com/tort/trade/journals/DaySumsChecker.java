package com.tort.trade.journals;

import com.tort.trade.model.Transition;

import java.util.*;

public class DaySumsChecker {
    public Map<Date, List<Transition>> invoke(Map<Date, List<Transition>> sortedTransitions) {
        for (Date date : sortedTransitions.keySet()) {
            final List<Transition> transitions = sortedTransitions.get(date);
            List<Transition> markDeleted = new ArrayList();
            for (Transition currentTransition : transitions) {
                    if(checkSums(currentTransition, transitions)){
                        markDeleted.addAll(delete(currentTransition, transitions));
                    }
            }
            transitions.removeAll(markDeleted);
        }

        removeEmptyDays(sortedTransitions);

        return sortedTransitions;
    }

    private void removeEmptyDays(Map<Date, List<Transition>> sortedTransitions) {
        for (Iterator<Date> iterator = sortedTransitions.keySet().iterator(); iterator.hasNext();) {
            Date date = iterator.next();
            final List<Transition> transitions = sortedTransitions.get(date);
            if(transitions.size() == 0)
                iterator.remove();
        }
    }

    private List<Transition> delete(Transition current, List<Transition> transitions) {
        List<Transition> result = new ArrayList();
        final TransitionKey currentKey = new TransitionKey(current);
        for (Iterator<Transition> iterator = transitions.iterator(); iterator.hasNext();) {
            Transition transition = iterator.next();
            final TransitionKey key = new TransitionKey(transition);
            if(key.equals(currentKey) || key.opposite(currentKey)){
                result.add(transition);
            }
        }

        return result;
    }

    public boolean checkSums(Transition current, List<Transition> transitions) {
        long sum = 0L;
        long oppositeSum = 0L;

        final TransitionKey currentKey = new TransitionKey(current);
        for (Transition transition : transitions) {
            final TransitionKey key = new TransitionKey(transition);
            if(key.equals(currentKey)){
                sum += transition.getQuant();
            }
            if(key.opposite(currentKey)){
                oppositeSum += transition.getQuant();
            }
        }

        return sum == oppositeSum;
    }

    public Map<TransitionKey, List<Transition>> group(List<Transition> transitions) {
        Map<TransitionKey, List<Transition>> grouped = new HashMap<TransitionKey, List<Transition>>();
        for (Transition transition : transitions) {
            TransitionKey transitionKey = new TransitionKey(transition);
            List<Transition> daysTransitions = grouped.get(transitionKey);
            if(daysTransitions == null){
                daysTransitions = new ArrayList<Transition>();
                grouped.put(transitionKey, daysTransitions);
            }
            daysTransitions.add(transition);
        }

        return grouped;
    }
}
