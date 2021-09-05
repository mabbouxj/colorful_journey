package net.mabbouxj.colorful_journey.utils;

public interface IPair<A, B> {

    A getA();

    B getB();

    static <A, B> IPair<A, B> of(A a, B b) {
        return new Impl<>(a, b);
    }

    class Impl<A, B> implements IPair<A, B> {

        private final A a;
        private final B b;

        private Impl(A a, B b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public A getA() {
            return a;
        }

        @Override
        public B getB() {
            return b;
        }

        @SuppressWarnings("rawtypes")
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof IPair)) return false;
            IPair pair = (IPair)o;
            return pair.getA().equals(a) && pair.getB().equals(b);
        }

        @Override
        public int hashCode() {
            return a.hashCode() ^ b.hashCode();
        }

    }

}
