/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.phenotips.data.similarity.internal;

import org.phenotips.data.FeatureMetadatum;
import org.phenotips.data.similarity.FeatureMetadatumSimilarityScorer;
import org.phenotips.data.similarity.internal.DefaultFeatureMetadatumSimilarityScorer;
import org.phenotips.data.similarity.internal.mocks.MockFeatureMetadatum;

import org.xwiki.component.manager.ComponentLookupException;
import org.xwiki.test.mockito.MockitoComponentMockingRule;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for the default {@link FeatureMetadatumSimilarityScorer} {@link DefaultFeatureMetadatumSimilarityScorer
 * generic implementation}.
 * 
 * @version $Id$
 */
public class DefaultFeatureMetadatumSimilarityScorerTest
{
    @Rule
    public final MockitoComponentMockingRule<FeatureMetadatumSimilarityScorer> mocker =
        new MockitoComponentMockingRule<FeatureMetadatumSimilarityScorer>(
            DefaultFeatureMetadatumSimilarityScorer.class);

    /** Same term should get the maximum score. */
    @Test
    public void testEqualValues() throws ComponentLookupException
    {
        FeatureMetadatum match = new MockFeatureMetadatum("ONTO:0001", "Low value", "range");
        FeatureMetadatum reference = new MockFeatureMetadatum("ONTO:0001", "Low value", "range");
        Assert.assertEquals(1.0, this.mocker.getComponentUnderTest().getScore(match, reference), 1.0E-5);
    }

    /** Different terms should get the minimum score. */
    @Test
    public void testDifferentValues() throws ComponentLookupException
    {
        FeatureMetadatum match = new MockFeatureMetadatum("ONTO:0001", "Low value", "range");
        FeatureMetadatum reference = new MockFeatureMetadatum("ONTO:0009", "High value", "range");
        Assert.assertEquals(-1.0, this.mocker.getComponentUnderTest().getScore(match, reference), 1.0E-5);
    }

    /** Different types of terms should get a zero score. */
    @Test
    public void testDifferentValueTypes() throws ComponentLookupException
    {
        FeatureMetadatum match = new MockFeatureMetadatum("ONTO:0001", "Low value", "range");
        FeatureMetadatum reference = new MockFeatureMetadatum("ONTO:0011", "Low impact", "impact");
        Assert.assertEquals(0.0, this.mocker.getComponentUnderTest().getScore(match, reference), 1.0E-5);
    }

    /** Missing reference should get a zero score. */
    @Test
    public void testMissingReference() throws ComponentLookupException
    {
        FeatureMetadatum match = new MockFeatureMetadatum("ONTO:0001", "Low value", "range");
        Assert.assertEquals(0.0, this.mocker.getComponentUnderTest().getScore(match, null), 1.0E-5);
    }

    /** Missing match should get a zero score. */
    @Test
    public void testMissingMatch() throws ComponentLookupException
    {
        FeatureMetadatum reference = new MockFeatureMetadatum("ONTO:0001", "Low value", "range");
        Assert.assertEquals(0.0, this.mocker.getComponentUnderTest().getScore(null, reference), 1.0E-5);
    }

    /** Missing both match and reference should get a zero score. */
    @Test
    public void testMissingMatchAndReference() throws ComponentLookupException
    {
        Assert.assertEquals(0.0, this.mocker.getComponentUnderTest().getScore(null, null), 1.0E-5);
    }
}
