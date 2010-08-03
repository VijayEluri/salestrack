package com.tort.trade.journals;

import com.tort.trade.model.Good;
import com.tort.trade.model.Sales;
import com.tort.trade.model.Transition;

class TransitionKey {
    private Good _good;
    private Sales _me;
    private Sales _from;
    private Sales _to;

    public TransitionKey(Good good, Sales me, Sales from, Sales to) {
        _good = good;
        _me = me;
        _from = from;
        _to = to;
    }

    public TransitionKey(Transition transition) {
        _good = transition.getGood();
        _me = transition.getMe();
        _from = transition.getFrom();
        _to = transition.getTo();
    }

    public Good getGood() {
        return _good;
    }

    public Sales getMe() {
        return _me;
    }

    public Sales getFrom() {
        return _from;
    }

    public Sales getTo() {
        return _to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransitionKey that = (TransitionKey) o;

        if (!_from.equals(that._from)) return false;
        if (!_good.equals(that._good)) return false;
        if (!_me.equals(that._me)) return false;
        if (!_to.equals(that._to)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _good.hashCode();
        result = 31 * result + _me.hashCode();
        result = 31 * result + _from.hashCode();
        result = 31 * result + _to.hashCode();
        return result;
    }

    public boolean opposite(TransitionKey transitionKey) {
        if (this == transitionKey) return false;
        if (transitionKey == null || getClass() != transitionKey.getClass()) return false;

        if (!_from.equals(transitionKey.getFrom())) return false;
        if (!_to.equals(transitionKey.getTo())) return false;
        if (!_good.equals(transitionKey.getGood())) return false;

        if (_me.equals(_from)) {
            return transitionKey.getMe().equals(transitionKey.getTo());
        } else {
            return transitionKey.getMe().equals(transitionKey.getFrom());
        }
    }
}
