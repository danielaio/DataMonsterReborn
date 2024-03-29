/*
 * Copyright 2007 Yusuke Yamamoto
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


import twitter4j.FilterQuery;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * <p>This is a code example of Twitter4J Streaming API - filter method support.<br>
 * Usage: java twitter4j.examples.stream.PrintFilterStream [follow(comma separated numerical user ids)] [track(comma separated filter terms)]<br>
 * </p>
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class PrintFilterStream {
	/**
	 * Main entry of this application.
	 *
	 * @param args follow(comma separated user ids) track(comma separated filter terms)
	 * @throws twitter4j.TwitterException
	 */
	public static void main(String[] args) throws TwitterException {

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		.setOAuthConsumerKey("EFvoJ3uiVR9oSYjrcBPb6g")
		.setOAuthConsumerSecret("jTwzY9t5lQRQ7YemhVxfCIBfpfgfloTVGRgvBq9w")
		.setOAuthAccessToken("216501896-LuyoBsr6bfLDP73O1LkbtvW5WJPxwNpoOi8Se8cD")
		.setOAuthAccessTokenSecret("LuftunWNHrhBn05xzkeOlxBGc2PlCFRo6bHe3e49lY");



		if (args.length < 1) {
			System.out.println("Usage: java twitter4j.examples.PrintFilterStream [follow(comma separated numerical user ids)] [track(comma separated filter terms)]");
			System.exit(-1);
		}

		StatusListener listener = new StatusListener() {
			public void onStatus(Status status) {
				System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
			}

			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
				System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
			}

			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
				System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
			}

			public void onScrubGeo(long userId, long upToStatusId) {
				System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
			}

			public void onException(Exception ex) {
				ex.printStackTrace();
			}
		};

		TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
		twitterStream.addListener(listener);
		ArrayList<Long> follow = new ArrayList<Long>();
		ArrayList<String> track = new ArrayList<String>();
		for (String arg : args) {
			if (isNumericalArgument(arg)) {
				for (String id : arg.split(",")) {
					follow.add(Long.parseLong(id));
				}
			} else {
				track.addAll(Arrays.asList(arg.split(",")));
			}
		}
		long[] followArray = new long[follow.size()];
		for (int i = 0; i < follow.size(); i++) {
			followArray[i] = follow.get(i);
		}
		String[] trackArray = track.toArray(new String[track.size()]);

		// filter() method internally creates a thread which manipulates TwitterStream and calls these adequate listener methods continuously.
		twitterStream.filter(new FilterQuery(0, followArray, trackArray));

		try {
			Thread.sleep(120000);
			twitterStream.cleanUp();
			twitterStream.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(0);
		}

	}

	private static boolean isNumericalArgument(String argument) {
		String args[] = argument.split(",");
		boolean isNumericalArgument = true;
		for (String arg : args) {
			try {
				Integer.parseInt(arg);
			} catch (NumberFormatException nfe) {
				isNumericalArgument = false;
				break;
			}
		}
		return isNumericalArgument;
	}
}
