/*
 * Copyright 2011-2012 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package test;

import java.util.UUID;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.examples.WordCount;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.springframework.util.Assert;

import static org.junit.Assert.*;

/**
 * @author Costin Leau
 */
public class SomeToolCopy extends Configured implements Tool {

	static {
		System.setProperty("org.springframework.data.tool.init", UUID.randomUUID().toString());
	}

	public static class CustomMapper extends WordCount.TokenizerMapper {
		public CustomMapper() {
			super();
		}
	}

	public final Job createJob(Configuration conf) throws Exception {
		Assert.notNull(conf);
		Job j = new Job(conf, "tool-test");
		j.setMapperClass(CustomMapper.class);
		j.setOutputKeyClass(Text.class);
		j.setOutputValueClass(IntWritable.class);

		FileInputFormat.addInputPath(j, new Path("/ide-test/input/word/"));
		FileOutputFormat.setOutputPath(j, new Path("/ide-test/runner/output/" + UUID.randomUUID().toString()));
		return j;
	}

	@Override
	public int run(String[] args) throws Exception {
		Job j = new SomeToolCopy().createJob(getConf());
		j.waitForCompletion(false);
		assertTrue("Job failed", j.isSuccessful());
		return Integer.valueOf(args[0]);
	}
}
