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

package de.geobe.util.association.test

import de.geobe.util.association.IGetOther
import de.geobe.util.association.IToAny
import de.geobe.util.association.ToMany
import de.geobe.util.association.ToOne

/**
 * Created by georg beier on 12.01.2018.
 */

/**
 * Origin of 1-X associations
 */
class One1 {

    /** bidirectional 1-1 association */
    private One2 one2bi
    private ToOne<One1, One2> toOne2bi = new ToOne<>(
            { this.@one2bi } as IToAny.IGet,
            { One2 o2 -> this.@one2bi = o2 } as IToAny.ISet,
            this,
            { One2 o2 -> o2.one1bi } as IGetOther
    )
    IToAny<One1> getOne2bi() { toOne2bi }

    /** unidirectional 1-1 association */
    private One2 one2uni
    private ToOne<One1, One2> toOne2uni = new ToOne<>(
            { this.@one2uni } as IToAny.IGet,
            { One2 o2 -> this.@one2uni = o2 } as IToAny.ISet,
            this, null
    )
    IToAny<One1> getOne2uni() { toOne2uni }

    /** bidirectional 1-N association */
    private List<Many2> many2bi = new ArrayList<>()
    private ToMany<One1, Many2> toMany2bi = new ToMany<>(
            { this.@many2bi } as IToAny.IGet,
            this,
            { Many2 m2 -> m2.one1bi } as IGetOther
    )

    IToAny<One1> getMany2bi() { toMany2bi }
}

/**
 * Target for to-one associations
 */
class One2 {

    /** bidirectional 1-1 association */
    private One1 one1bi
    private ToOne<One2, One1> toOne1bi = new ToOne<>(
            { this.@one1bi } as IToAny.IGet,
            { One1 o1 -> this.@one1bi = o1 } as IToAny.ISet,
            this,
            { One1 o1 -> o1.one2bi } as IGetOther
    )

    IToAny<One1> getOne1bi() { toOne1bi }
}

/**
 * Target for unidirectional to-N associations
 */
class Any {
    def any = 'Any get your gun ;)'
}

/**
 * Origin of to-Many associations
 */
class Many1 {

    /** unidirectional to-N association */
    private List<Any> many2uni = new ArrayList<>()
    private ToMany<Many1, Any> toAnyUni = new ToMany<>(
            { this.@many2uni } as IToAny.IGet,
            this, null
    )

    IToAny<Many1> getAnybi() { toAnyUni }

    /** bidirectional M-N association */
    private List<Many2> many2bi = new ArrayList<>()
    private ToMany<Many1, Many2> toMany2bi = new ToMany<>(
            { this.@many2bi } as IToAny.IGet,
            this,
            { Many2 m2 -> m2.many1bi } as IGetOther
    )

    IToAny<One1> getMany2bi() { toMany2bi }
}

/**
 * Target for to-Many associations
 */
class Many2 {

    /** bidirectional N-1 association */
    private One1 one1bi
    private ToOne<Many2, One1> toOne1bi = new ToOne<>(
            { this.@one1bi } as IToAny.IGet,
            { One1 o1 -> this.@one1bi = o1 } as IToAny.ISet,
            this,
            { One1 o1 -> o1.many2bi } as IGetOther
    )

    IToAny<One1> getOne1bi() { toOne1bi }

    /** bidirectional M-N association */
    private List<Many1> many1bi = new ArrayList<>()
    private ToMany<Many2, Many2> toMany1bi = new ToMany<>(
            { this.@many1bi } as IToAny.IGet,
            this,
            { Many1 m1 -> m1.many2bi } as IGetOther
    )

    IToAny<Many1> getMany1bi() { toMany1bi }
}

