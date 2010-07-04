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
public class IncomeOperationTest extends OperationTest {
    private Sales _me;
    private Sales _supplier;

    @Override
    protected Operation positiveSetUp() {
        _me = new Sales("me");
        _supplier = new Sales("supplier");

        Session session = createMock(Session.class);
        expect(session.load(eq(Good.class), anyLong())).andReturn(new Good());
        expect(session.load(eq(Sales.class), anyLong())).andReturn(_supplier);
        replay(session);

        IncomeOperation.Matcher matcher = createMock(IncomeOperation.Matcher.class);
        expect(matcher.getBuyPrice()).andReturn(new BigDecimal(200));
        expect(matcher.getSellPrice()).andReturn(new BigDecimal(300));
        expect(matcher.getQuant()).andReturn(10L);
        replay(matcher);

        return new IncomeOperation(session, _me, matcher);
    }

    public void incomeTransition(){
        final Operation operation = positiveSetUp();

        TransitionTO transitionTO = new TransitionTO();
        transitionTO.setText("200/300*10");

        final Transition transition = operation.createTransition(transitionTO);

        assertEquals(transition.getBuyPrice(), new BigDecimal(200));
        assertNotNull(transition.getGood());
        assertNotNull(transition.getDate());
        assertEquals(transition.getSellPrice(), new BigDecimal(300));
        assertEquals(transition.getQuant(), new Long(10));
        assertEquals(transition.getTo(), transition.getMe());
        assertEquals(transition.getMe(), _me);
        assertEquals(transition.getFrom(), _supplier);
    }
}
