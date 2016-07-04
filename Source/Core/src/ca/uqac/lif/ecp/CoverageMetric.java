/*
    Log trace triaging and etc.
    Copyright (C) 2016 Sylvain Hallé

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ca.uqac.lif.ecp;

import java.util.Set;

public abstract class CoverageMetric<T,V> 
{
	/**
	 * Creates a new coverage metric instance
	 * @param traces The set of traces we wish to measure coverage for
	 */
	public CoverageMetric()
	{
		super();
	}
	
	/**
	 * Gets the coverage of the set of traces according to the metric
	 * @param traces The set of traces we wish to measure coverage for
	 * @return The coverage
	 */
	public abstract V getCoverage(Set<Trace<T>> traces);
}
