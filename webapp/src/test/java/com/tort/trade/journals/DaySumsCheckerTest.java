package com.tort.trade.journals;

import com.tort.trade.model.Good;
import com.tort.trade.model.Sales;
import com.tort.trade.model.Transition;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

@Test
public class DaySumsCheckerTest {
    private DaySumsChecker _checker;
    private Good _shirt;
    private Good _hat;
    private Sales _masha;
    private Sales _sasha;
    private Sales _gena;

    public void testInvoke() throws Exception {
        final HashMap<Date, List<Transition>> map = new HashMap<Date, List<Transition>>();
        final List<Transition> transitions = new ArrayList<Transition>();

        Transition transition = new Transition();
        transition.setMe(_masha);
        transition.setFrom(_masha);
        transition.setTo(_sasha);
        transition.setQuant(1L);
        transition.setGood(_shirt);
        transitions.add(transition);
        transitions.add(transition);

        transition = new Transition();
        transition.setMe(_sasha);
        transition.setFrom(_masha);
        transition.setTo(_sasha);
        transition.setQuant(2L);
        transition.setGood(_shirt);
        transitions.add(transition);

        transition = new Transition();
        transition.setMe(_masha);
        transition.setFrom(_masha);
        transition.setTo(_gena);
        transition.setQuant(3L);
        transition.setGood(_shirt);
        transitions.add(transition);

        transition = new Transition();
        transition.setMe(_masha);
        transition.setFrom(_masha);
        transition.setTo(_sasha);
        transition.setQuant(5L);
        transition.setGood(_hat);
        transitions.add(transition);

        final Date date = new Date();
        map.put(date, transitions);

        final Map<Date,List<Transition>> result = _checker.invoke(map);

        assertNotNull(result);
        assertEquals(result.get(date).size(), 2);
    }

    public void checkSums(){
        List<Transition> transitions = new ArrayList<Transition>();

        Transition transition = new Transition();
        transition.setGood(_shirt);
        transition.setMe(_gena);
        transition.setFrom(_gena);
        transition.setTo(_masha);
        transition.setQuant(1L);
        transitions.add(transition);

        transition = new Transition();
        transition.setGood(_shirt);
        transition.setMe(_masha);
        transition.setFrom(_gena);
        transition.setTo(_masha);
        transition.setQuant(1L);
        transitions.add(transition);

        final boolean result = _checker.checkSums(transition, transitions);

        assertTrue(result);
    }

    public void testGroup(){
        final ArrayList<Transition> transitions = new ArrayList<Transition>();

        Transition transition = new Transition();
        transition.setGood(_shirt);
        transition.setMe(_gena);
        transition.setFrom(_gena);
        transition.setTo(_masha);
        transitions.add(transition);

        transition = new Transition();
        transition.setGood(_shirt);
        transition.setMe(_gena);
        transition.setFrom(_sasha);
        transition.setTo(_gena);
        transitions.add(transition);

        transition = new Transition();
        transition.setGood(_shirt);
        transition.setMe(_gena);
        transition.setFrom(_gena);
        transition.setTo(_masha);
        transitions.add(transition);

        final Map<TransitionKey, List<Transition>> result = _checker.group(transitions);

        assertNotNull(result);
        assertEquals(result.keySet().size(), 2);
    }

    @BeforeMethod
    protected void setUp() throws Exception {
        _checker = new DaySumsChecker();
        _shirt = new Good();
        _hat = new Good();
        _masha = new Sales();
        _sasha = new Sales();
        _gena = new Sales();
    }
}
