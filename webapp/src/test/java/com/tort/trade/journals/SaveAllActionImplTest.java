package com.tort.trade.journals;

import static org.easymock.EasyMock.*;
import static org.testng.AssertJUnit.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;
import org.testng.annotations.Test;

import com.tort.trade.model.Sales;
import com.tort.trade.model.Transition;

@Test
public class SaveAllActionImplTest extends ActionTest {

    public void newActionNegativeNoDataParam() {
        try {
            Map<String, String[]> params = new HashMap<String, String[]>();
            params.put("me", new String[]{"1"});

            new SaveAllAction(params, null, null);
            fail();
        } catch (IllegalArgumentException ignored) {

        }
    }

    public void newActionNegativeNoMeParam() {
        try {
            Map<String, String[]> params = new HashMap<String, String[]>();
            params.put("data", new String[]{"[{_goodId: 1, _text: \"драссте вам\", _lid: 1}]"});

            new SaveAllAction(params, null, null);
            fail();
        } catch (IllegalArgumentException ignored) {

        }
    }

    public void newActionNegativeZeroLengthMeParam() {
        try {
            Map<String, String[]> params = new HashMap<String, String[]>();
            params.put("data", new String[]{"[{_goodId: 1, _text: \"драссте вам\", _lid: 1}]"});
            params.put("me", new String[]{});

            new SaveAllAction(params, null, null);
            fail();
        } catch (IllegalArgumentException ignored) {

        }
    }

    public void newActionNullParams() {
        try {
            new SaveAllAction(null, createMock(Session.class), createMock(TransitionConverterLookup.class));
            fail();
        } catch (IllegalArgumentException ignored) {

        }
    }

    public void newActionNullSession() {
        try {
            new SaveAllAction(createParams(), null, createMock(TransitionConverterLookup.class));
            fail();
        } catch (IllegalArgumentException ignored) {

        }
    }

    public void newActionNullLookup() {
        try {
            new SaveAllAction(createParams(), createMock(Session.class), null);
            fail();
        } catch (IllegalArgumentException ignored) {

        }
    }

    public void newActionPositive() {
        Session session = createMock(Session.class);
        expect(session.load(eq(Sales.class), isA(Long.class))).andReturn(new Sales("test"));
        replay(session);

        new SaveAllAction(createParams(), session, createMock(TransitionConverterLookup.class));
    }

    private Map<String, String[]> createParams() {
        Map<String, String[]> params = new HashMap<String, String[]>();
        params.put("data", new String[]{"[{_goodId: 1, _text: \"драссте вам\", _lid: 1}]"});
        params.put("me", new String[]{"1"});
        return params;
    }

    public void actImpl() {
        Action action = positiveSetUp();

        View result = action.act();

        assertNotNull(result);
    }

    @SuppressWarnings({"ThrowableInstanceNeverThrown"})
    protected Action positiveSetUp() {
        Session mockSession = createMock(Session.class);
        TransitionConverter converter = createMock(TransitionConverter.class);
        TransitionConverterLookup converterLookup = createMock(TransitionConverterLookup.class);

        Map<String, String[]> params = new HashMap<String, String[]>();
        params.put("data", new String[]{"[{_goodId: 5, _text: \"bad_data\", _lid: 5}, {_goodId: 1, _text: \"good_data\", _lid: 1}, {_goodId: 2, _text: \"bad_data\", _lid: 2}]"});
        params.put("me", new String[]{"1"});

        SaveAllAction action = new SaveAllAction(params, mockSession, converterLookup);

        expect(mockSession.load(eq(Sales.class), isA(Long.class))).andReturn(new Sales("test"));
        expect(converterLookup.getTransitionConverter(isA(Session.class), isA(Sales.class))).andReturn(converter);
        expect(mockSession.save(isA(Transition.class))).andStubReturn(1L);
        try {
            expect(converter.convertToEntity(isA(TransitionTO.class))).andThrow(new ConvertTransitionException());
            ArrayList<Transition> transitions = new ArrayList<Transition>();
            transitions.add(new Transition());
            expect(converter.convertToEntity(isA(TransitionTO.class))).andReturn(transitions);
            expect(converter.convertToEntity(isA(TransitionTO.class))).andThrow(new ConvertTransitionException());
        } catch (ConvertTransitionException e) {
            e.printStackTrace();
        }
        mockSession.flush();
        replay(converter, converterLookup, mockSession);

        return action;
    }
}

