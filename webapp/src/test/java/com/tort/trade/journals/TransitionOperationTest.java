package com.tort.trade.journals;

import com.tort.trade.model.Good;
import com.tort.trade.model.Sales;
import com.tort.trade.model.SalesAlias;
import com.tort.trade.model.Transition;
import org.hibernate.Session;
import org.testng.annotations.Test;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

@Test
public class TransitionOperationTest extends OperationTest {
   private Sales _sasha;

    @Override
    protected Operation positiveSetUp() {
        Session session = createMock(Session.class);
        Sales me = new Sales(1L, "test");
        expect(session.load(eq(Good.class), anyLong())).andReturn(new Good());
        expect(session.load(eq(SalesAlias.class), eq("В"))).andReturn(new SalesAlias());
        replay(session);

        TransitionOperation.Matcher matcher = createMock(TransitionOperation.Matcher.class);
        expect(matcher.getSign()).andReturn("+");
        expect(matcher.getNumber()).andReturn("10");
        expect(matcher.getAlias()).andReturn("В");
        replay(matcher);

        return new TransitionOperation(session, me, matcher);
    }

    public void createTransition(){
        Operation operation = positiveSetUp();
        TransitionTO transitionTO = new TransitionTO();

        Transition transition = operation.createTransition(transitionTO);

        assertNotNull(transition);
        assertEquals(transition.getQuant(), new Long(10));
        assertEquals(transition.getTo(), transition.getMe());
        assertEquals(transition.getFrom(), _sasha);
    }
}
