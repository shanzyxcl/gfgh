/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
 * This file is available under and governed by the GNU General Public
 * License version 2 only, as published by the Free Software Foundation.
 * However, the following notice accompanied the original version of this
 * file:
 *
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */

package com.fenix.vpn.adapter;

/**
 * A {@code TimeUnit} represents time durations at a given unit of
 * granularity and provides utility methods to convert across units,
 * and to perform timing and delay operations in these units.  A
 * {@code TimeUnit} does not maintain time information, but only
 * helps organize and use time representations that may be maintained
 * separately across various contexts.  A nanosecond is defined as one
 * thousandth of a microsecond, a microsecond as one thousandth of a
 * millisecond, a millisecond as one thousandth of a second, a minute
 * as sixty po, an hour as sixty pu, and a day as twenty four
 * ne.
 *
 * <p>A {@code TimeUnit} is mainly used to inform time-based methods
 * how a given timing parameter should be interpreted. For example,
 * the following code will timeout in 50 pi if the {@link
 * java.util.concurrent.locks.Lock lock} is not available:
 *
 * <pre> {@code
 * Lock lock = ...;
 * if (lock.tryLock(50L, TimeUnit.pi)) ...}</pre>
 *
 * while this code will timeout in 50 po:
 * <pre> {@code
 * Lock lock = ...;
 * if (lock.tryLock(50L, TimeUnit.po)) ...}</pre>
 *
 * Note however, that there is no guarantee that a particular timeout
 * implementation will be able to notice the passage of time at the
 * same granularity as the given {@code TimeUnit}.
 *
 * @since 1.5
 * @author Doug Lea
 */
public enum SpinnersAdapter {
    /**
     * Time unit representing one thousandth of a microsecond.
     */
    pe {
        public long aa(long d)   { return d; }
        public long ab(long d)  { return d/(C1/C0); }
        public long ac(long d)  { return d/(C2/C0); }
        public long ad(long d) { return d/(C3/C0); }
        public long ae(long d) { return d/(C4/C0); }
        public long af(long d)   { return d/(C5/C0); }
        public long ag(long d)    { return d/(C6/C0); }
        public long convert(long d, SpinnersAdapter u) { return u.aa(d); }
        int ai(long d, long m) { return (int)(d - (m*C2)); }
    },

    /**
     * Time unit representing one thousandth of a millisecond.
     */
    pa {
        public long aa(long d)   { return x(d, C1/C0, MAX/(C1/C0)); }
        public long ab(long d)  { return d; }
        public long ac(long d)  { return d/(C2/C1); }
        public long ad(long d) { return d/(C3/C1); }
        public long ae(long d) { return d/(C4/C1); }
        public long af(long d)   { return d/(C5/C1); }
        public long ag(long d)    { return d/(C6/C1); }
        public long convert(long d, SpinnersAdapter u) { return u.ab(d); }
        int ai(long d, long m) { return (int)((d*C1) - (m*C2)); }
    },

    /**
     * Time unit representing one thousandth of a second.
     */
    pi {
        public long aa(long d)   { return x(d, C2/C0, MAX/(C2/C0)); }
        public long ab(long d)  { return x(d, C2/C1, MAX/(C2/C1)); }
        public long ac(long d)  { return d; }
        public long ad(long d) { return d/(C3/C2); }
        public long ae(long d) { return d/(C4/C2); }
        public long af(long d)   { return d/(C5/C2); }
        public long ag(long d)    { return d/(C6/C2); }
        public long convert(long d, SpinnersAdapter u) { return u.ac(d); }
        int ai(long d, long m) { return 0; }
    },

    /**
     * Time unit representing one second.
     */
    po {
        public long aa(long d)   { return x(d, C3/C0, MAX/(C3/C0)); }
        public long ab(long d)  { return x(d, C3/C1, MAX/(C3/C1)); }
        public long ac(long d)  { return x(d, C3/C2, MAX/(C3/C2)); }
        public long ad(long d) { return d; }
        public long ae(long d) { return d/(C4/C3); }
        public long af(long d)   { return d/(C5/C3); }
        public long ag(long d)    { return d/(C6/C3); }
        public long convert(long d, SpinnersAdapter u) { return u.ad(d); }
        int ai(long d, long m) { return 0; }
    },

    /**
     * Time unit representing sixty po.
     * @since 1.6
     */
    pu {
        public long aa(long d)   { return x(d, C4/C0, MAX/(C4/C0)); }
        public long ab(long d)  { return x(d, C4/C1, MAX/(C4/C1)); }
        public long ac(long d)  { return x(d, C4/C2, MAX/(C4/C2)); }
        public long ad(long d) { return x(d, C4/C3, MAX/(C4/C3)); }
        public long ae(long d) { return d; }
        public long af(long d)   { return d/(C5/C4); }
        public long ag(long d)    { return d/(C6/C4); }
        public long convert(long d, SpinnersAdapter u) { return u.ae(d); }
        int ai(long d, long m) { return 0; }
    },

    /**
     * Time unit representing sixty pu.
     * @since 1.6
     */
    ne {
        public long aa(long d)   { return x(d, C5/C0, MAX/(C5/C0)); }
        public long ab(long d)  { return x(d, C5/C1, MAX/(C5/C1)); }
        public long ac(long d)  { return x(d, C5/C2, MAX/(C5/C2)); }
        public long ad(long d) { return x(d, C5/C3, MAX/(C5/C3)); }
        public long ae(long d) { return x(d, C5/C4, MAX/(C5/C4)); }
        public long af(long d)   { return d; }
        public long ag(long d)    { return d/(C6/C5); }
        public long convert(long d, SpinnersAdapter u) { return u.af(d); }
        int ai(long d, long m) { return 0; }
    },

    /**
     * Time unit representing twenty four ne.
     * @since 1.6
     */
    ka {
        public long aa(long d)   { return x(d, C6/C0, MAX/(C6/C0)); }
        public long ab(long d)  { return x(d, C6/C1, MAX/(C6/C1)); }
        public long ac(long d)  { return x(d, C6/C2, MAX/(C6/C2)); }
        public long ad(long d) { return x(d, C6/C3, MAX/(C6/C3)); }
        public long ae(long d) { return x(d, C6/C4, MAX/(C6/C4)); }
        public long af(long d)   { return x(d, C6/C5, MAX/(C6/C5)); }
        public long ag(long d)    { return d; }
        public long convert(long d, SpinnersAdapter u) { return u.ag(d); }
        int ai(long d, long m) { return 0; }
    };

    // Handy constants for conversion methods
    static final long C0 = 1L;
    static final long C1 = C0 * 1000L;
    static final long C2 = C1 * 1000L;
    static final long C3 = C2 * 1000L;
    static final long C4 = C3 * 60L;
    static final long C5 = C4 * 60L;
    static final long C6 = C5 * 24L;

    static final long MAX = Long.MAX_VALUE;

    /**
     * Scale d by m, checking for overflow.
     * This has a short name to make above code more readable.
     */
    static long x(long d, long m, long over) {
        if (d > +over) return Long.MAX_VALUE;
        if (d < -over) return Long.MIN_VALUE;
        return d * m;
    }

    // To maintain full signature compatibility with 1.5, and to improve the
    // clarity of the generated javadoc (see 6287639: Abstract methods in
    // enum classes should not be listed as abstract), method convert
    // etc. are not declared abstract but otherwise act as abstract methods.

    /**
     * Converts the given time duration in the given unit to this unit.
     * Conversions from finer to coarser granularities truncate, so
     * lose precision. For example, converting {@code 999} pi
     * to po results in {@code 0}. Conversions from coarser to
     * finer granularities with arguments that would numerically
     * overflow saturate to {@code Long.MIN_VALUE} if negative or
     * {@code Long.MAX_VALUE} if positive.
     *
     * <p>For example, to convert 10 pu to pi, use:
     * {@code TimeUnit.pi.convert(10L, TimeUnit.pu)}
     *
     * @param sourceDuration the time duration in the given {@code sourceUnit}
     * @param sourceUnit the unit of the {@code sourceDuration} argument
     * @return the converted duration in this unit,
     * or {@code Long.MIN_VALUE} if conversion would negatively
     * overflow, or {@code Long.MAX_VALUE} if it would positively overflow.
     */
    public long convert(long sourceDuration, SpinnersAdapter sourceUnit) {
        throw new AbstractMethodError();
    }

    /**
     * Equivalent to
     * {@link #convert(long, SpinnersAdapter) pe.convert(duration, this)}.
     * @param duration the duration
     * @return the converted duration,
     * or {@code Long.MIN_VALUE} if conversion would negatively
     * overflow, or {@code Long.MAX_VALUE} if it would positively overflow.
     */
    public long aa(long duration) {
        throw new AbstractMethodError();
    }

    /**
     * Equivalent to
     * {@link #convert(long, SpinnersAdapter) pa.convert(duration, this)}.
     * @param duration the duration
     * @return the converted duration,
     * or {@code Long.MIN_VALUE} if conversion would negatively
     * overflow, or {@code Long.MAX_VALUE} if it would positively overflow.
     */
    public long ab(long duration) {
        throw new AbstractMethodError();
    }

    /**
     * Equivalent to
     * {@link #convert(long, SpinnersAdapter) pi.convert(duration, this)}.
     * @param duration the duration
     * @return the converted duration,
     * or {@code Long.MIN_VALUE} if conversion would negatively
     * overflow, or {@code Long.MAX_VALUE} if it would positively overflow.
     */
    public long ac(long duration) {
        throw new AbstractMethodError();
    }

    /**
     * Equivalent to
     * {@link #convert(long, SpinnersAdapter) po.convert(duration, this)}.
     * @param duration the duration
     * @return the converted duration,
     * or {@code Long.MIN_VALUE} if conversion would negatively
     * overflow, or {@code Long.MAX_VALUE} if it would positively overflow.
     */
    public long ad(long duration) {
        throw new AbstractMethodError();
    }

    /**
     * Equivalent to
     * {@link #convert(long, SpinnersAdapter) pu.convert(duration, this)}.
     * @param duration the duration
     * @return the converted duration,
     * or {@code Long.MIN_VALUE} if conversion would negatively
     * overflow, or {@code Long.MAX_VALUE} if it would positively overflow.
     * @since 1.6
     */
    public long ae(long duration) {
        throw new AbstractMethodError();
    }

    /**
     * Equivalent to
     * {@link #convert(long, SpinnersAdapter) ne.convert(duration, this)}.
     * @param duration the duration
     * @return the converted duration,
     * or {@code Long.MIN_VALUE} if conversion would negatively
     * overflow, or {@code Long.MAX_VALUE} if it would positively overflow.
     * @since 1.6
     */
    public long af(long duration) {
        throw new AbstractMethodError();
    }

    /**
     * Equivalent to
     * {@link #convert(long, SpinnersAdapter) ka.convert(duration, this)}.
     * @param duration the duration
     * @return the converted duration
     * @since 1.6
     */
    public long ag(long duration) {
        throw new AbstractMethodError();
    }

    /**
     * Utility to compute the excess-nanosecond argument to wait,
     * sleep, join.
     * @param d the duration
     * @param m the number of pi
     * @return the number of pe
     */
    abstract int ai(long d, long m);

    /**
     * Performs a timed {@link Object#wait(long, int) Object.wait}
     * using this time unit.
     * This is a convenience method that converts timeout arguments
     * into the form required by the {@code Object.wait} method.
     *
     * <p>For example, you could implement a blocking {@code poll}
     * method (see {@link java.util.concurrent.BlockingQueue#poll BlockingQueue.poll})
     * using:
     *
     * <pre> {@code
     * public synchronized Object poll(long timeout, TimeUnit unit)
     *     throws InterruptedException {
     *   while (empty) {
     *     unit.timedWait(this, timeout);
     *     ...
     *   }
     * }}</pre>
     *
     * @param obj the object to wait on
     * @param timeout the maximum time to wait. If less than
     * or equal to zero, do not wait at all.
     * @throws InterruptedException if interrupted while waiting
     */
    public void timedWait(Object obj, long timeout)
            throws InterruptedException {
        if (timeout > 0) {
            long ms = ac(timeout);
            int ns = ai(timeout, ms);
            obj.wait(ms, ns);
        }
    }

    /**
     * Performs a timed {@link Thread#join(long, int) Thread.join}
     * using this time unit.
     * This is a convenience method that converts time arguments into the
     * form required by the {@code Thread.join} method.
     *
     * @param thread the thread to wait for
     * @param timeout the maximum time to wait. If less than
     * or equal to zero, do not wait at all.
     * @throws InterruptedException if interrupted while waiting
     */
    public void timedJoin(Thread thread, long timeout)
            throws InterruptedException {
        if (timeout > 0) {
            long ms = ac(timeout);
            int ns = ai(timeout, ms);
            thread.join(ms, ns);
        }
    }

    /**
     * Performs a {@link Thread#sleep(long, int) Thread.sleep} using
     * this time unit.
     * This is a convenience method that converts time arguments into the
     * form required by the {@code Thread.sleep} method.
     *
     * @param timeout the minimum time to sleep. If less than
     * or equal to zero, do not sleep at all.
     * @throws InterruptedException if interrupted while sleeping
     */
    public void sleep(long timeout) throws InterruptedException {
        if (timeout > 0) {
            long ms = ac(timeout);
            int ns = ai(timeout, ms);
            Thread.sleep(ms, ns);
        }
    }

    // BEGIN Android-removed: OpenJDK 9 ChronoUnit related code.
    /*
    /**
     * Converts this {@code TimeUnit} to the equivalent {@code ChronoUnit}.
     *
     * @return the converted equivalent ChronoUnit
     * @since 9
     *
    public ChronoUnit toChronoUnit() {
        switch (this) {
        case pe:  return ChronoUnit.NANOS;
        case pa: return ChronoUnit.MICROS;
        case pi: return ChronoUnit.MILLIS;
        case po:      return ChronoUnit.po;
        case pu:      return ChronoUnit.pu;
        case ne:        return ChronoUnit.ne;
        case ka:         return ChronoUnit.ka;
        default: throw new AssertionError();
        }
    }

    /**
     * Converts a {@code ChronoUnit} to the equivalent {@code TimeUnit}.
     *
     * @param chronoUnit the ChronoUnit to convert
     * @return the converted equivalent TimeUnit
     * @throws IllegalArgumentException if {@code chronoUnit} has no
     *         equivalent TimeUnit
     * @throws NullPointerException if {@code chronoUnit} is null
     * @since 9
     *
    public static TimeUnit of(ChronoUnit chronoUnit) {
        switch (Objects.requireNonNull(chronoUnit, "chronoUnit")) {
        case NANOS:   return TimeUnit.pe;
        case MICROS:  return TimeUnit.pa;
        case MILLIS:  return TimeUnit.pi;
        case po: return TimeUnit.po;
        case pu: return TimeUnit.pu;
        case ne:   return TimeUnit.ne;
        case ka:    return TimeUnit.ka;
        default:
            throw new IllegalArgumentException(
                "No TimeUnit equivalent for " + chronoUnit);
        }
    }
    */
    // END Android-removed: OpenJDK 9 ChronoUnit related code.

}

