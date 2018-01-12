/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018.  Georg Beier. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.geobe.util.association;

import java.util.Collection;

/**
 * @author georg beier
 * <p>
 * Interface for implementation of to-N and to-1 associations
 */
public interface IToAny<THERE> {
    /**
     * add element to association
     *
     * @param other new element
     */
    void add(THERE other);

    /**
     * add a collection of elements to the association
     * @param others elements to add
     */
    void add(Collection<THERE> others);

    /**
     * add an array of elements to the association
     *
     * @param others elements to add
     */
    void add(THERE[] others);

    /**
     * remove element from association
     *
     * @param other existing element
     */
    void remove(THERE other);

    /**
     * Fetch one element of the association
     *
     * @return an element, if exists, else null
     */
    THERE getOne();

    /**
     * Fetch a collection of all elements of the association
     *
     * @return all elements
     */
    Collection<THERE> getAll();

    /**
     * Remove all elements from the association
     */
    void removeAll();

    /**
     * is replaced by a closure that gets local private field of surrounding object
     */
    @FunctionalInterface
    interface IGet<T> {
        T get();
    }

    /**
     * is replaced by a closure that ds setal private field of surrounding object
     */
    @FunctionalInterface
    interface ISet<T> {
        void set(T value);
    }
}
