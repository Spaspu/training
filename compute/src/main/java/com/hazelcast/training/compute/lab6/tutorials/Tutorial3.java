/*
 * Copyright (c) 2008-2013, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.training.compute.lab6.tutorials;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IList;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import com.hazelcast.training.compute.lab6.model.Person;
import com.hazelcast.training.compute.lab6.tutorials.impl.StateBasedCountMapper;
import com.hazelcast.training.compute.lab6.tutorials.impl.ToStringPrettyfier;

public class Tutorial3
        implements Tutorial {

    @Override
    public void execute(HazelcastInstance hazelcastInstance)
            throws Exception {

        JobTracker jobTracker = hazelcastInstance.getJobTracker("default");

        IList<Person> list = hazelcastInstance.getList("persons");
        KeyValueSource<String, Person> source = KeyValueSource.fromList(list);

        Job<String, Person> job = jobTracker.newJob(source);

        // Collect all people by state
        ICompletableFuture future = job.mapper(new StateBasedCountMapper()).submit();

        // Count people by state
        // ICompletableFuture future = job.mapper(new StateBasedCountMapper()).reducer(new CountReducerFactory()).submit();

        // Same as above but with precalculation per node
        // ICompletableFuture future = job.mapper(new StateBasedCountMapper()).combiner(new CountCombinerFactory())
        //                                .reducer(new CountReducerFactory()).submit();

        System.out.println(ToStringPrettyfier.toString(future.get()));
    }
}
