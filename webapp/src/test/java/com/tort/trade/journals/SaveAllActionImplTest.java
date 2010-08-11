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
            final Session session = createMock(Session.class);
            TransitionConversation conversation = createMock(TransitionConversation.class);
            expect(conversation.getHibernateSession()).andReturn(session);
            replay(conversation);

            new SaveAllAction(null, conversation, createMock(TransitionConverterLookup.class));
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
            final Session session = createMock(Session.class);
            TransitionConversation conversation = createMock(TransitionConversation.class);
            expect(conversation.getHibernateSession()).andReturn(session);
            replay(conversation);

            new SaveAllAction(createParams(), conversation, null);
            fail();
        } catch (IllegalArgumentException ignored) {

        }
    }

    public void newActionPositive() {
        Session session = createMock(Session.class);
        expect(session.load(eq(Sales.class), isA(Long.class))).andReturn(new Sales("test"));
        replay(session);

        TransitionConversation conversation = createMock(TransitionConversation.class);
        expect(conversation.getHibernateSession()).andReturn(session);
        replay(conversation);

        new SaveAllAction(createParams(), conversation, createMock(TransitionConverterLookup.class));
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
        TransitionConverter converter = createMock(TransitionConverter.class);
        TransitionConverterLookup converterLookup = createMock(TransitionConverterLookup.class);

        Map<String, String[]> params = new HashMap<String, String[]>();
        params.put("data", new String[]{"[{_goodId: 5, _text: \"bad_data\", _lid: 5}, {_goodId: 1, _text: \"good_data\", _lid: 1}, {_goodId: 2, _text: \"bad_data\", _lid: 2}]"});
        params.put("me", new String[]{"1"});

        final Session session = createMock(Session.class);
        TransitionConversation conversation = createMock(TransitionConversation.class);
        expect(conversation.getHibernateSession()).andReturn(session);
        replay(conversation);

        SaveAllAction action = new SaveAllAction(params, conversation, converterLookup);

        expect(session.load(eq(Sales.class), isA(Long.class))).andReturn(new Sales("test"));
        expect(converterLookup.getTransitionConverter(isA(Session.class), isA(Sales.class))).andReturn(converter);
        expect(session.save(isA(Transition.class))).andStubReturn(1L);
        try {
            expect(converter.convertToEntity(isA(TransitionTO.class))).andThrow(new ConvertTransitionException());
            ArrayList<Transition> transitions = new ArrayList<Transition>();
            transitions.add(new Transition());
            expect(converter.convertToEntity(isA(TransitionTO.class))).andReturn(transitions);
            expect(converter.convertToEntity(isA(TransitionTO.class))).andThrow(new ConvertTransitionException());
        } catch (ConvertTransitionException e) {
            e.printStackTrace();
        }
        session.flush();
        replay(converter, converterLookup, session);

        return action;
    }
}

