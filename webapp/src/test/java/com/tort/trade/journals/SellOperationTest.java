package com.tort.trade.journals;

import com.tort.trade.model.Good;
import com.tort.trade.model.Sales;
import com.tort.trade.model.Transition;
import org.hibernate.Session;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

@Test
public class SellOperationTest extends OperationTest {
    private Sales _me;
    private Sales _customer;

    @Override
    protected Operation positiveSetUp() {
        _me = new Sales("me");
        _customer = new Sales("customer");

        Session session = createMock(Session.class);
        expect(session.load(eq(Sales.class), anyLong())).andReturn(_customer);
        expect(session.load(eq(Good.class), anyLong())).andReturn(new Good());
        replay(session);

        SellOperation.FullMatcher matcher = createMock(SellOperation.FullMatcher.class);
        expect(matcher.getSellPrice()).andReturn(new BigDecimal(250));
        expect(matcher.getQuant()).andReturn(3L);
        replay(matcher);

        return new SellOperation(session, _me, matcher);
    }

    public void sellTransition(){
        TransitionTO transitionTO = new TransitionTO();
        transitionTO.setText("250*3");

        Operation operation = positiveSetUp();
        Transition transition = operation.createTransition(transitionTO);

        assertEquals(transition.getFrom(), transition.getMe());
        assertEquals(transition.getSellPrice(), new BigDecimal(250));
        assertEquals(transition.getQuant(), new Long(3));
        assertEquals(transition.getMe(), _me);
        assertEquals(transition.getTo(), _customer);
        assertNotNull(transition.getDate());
        assertNotNull(transition.getGood());
    }
}
