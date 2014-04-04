package com.github.okapies.hoc2js;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.ConfigSyntax;
import com.typesafe.config.ConfigRenderOptions;
import com.typesafe.config.ConfigResolveOptions;

/**
 * Very simple utility for converting from Typesafe's HOCON to JSON.
 * See https://github.com/typesafehub/config/blob/master/HOCON.md
 */
public class Hoc2Js {
	public static void main(String[] args) {
		Config config = ConfigFactory.empty();

		// parse
		ConfigParseOptions parseOpts = ConfigParseOptions.defaults()
			.setSyntax(ConfigSyntax.CONF); // HOCON
		if (args.length > 0) {
			for (String filename : args) {
				config = config.withFallback(ConfigFactory.parseFile(new File(filename)));
			}
		} else {
			// read from stdin instead of .conf files
			config = ConfigFactory.parseReader(
				new BufferedReader(new InputStreamReader(System.in)));
		}

		// resolve
		ConfigResolveOptions resolveOpts = ConfigResolveOptions.defaults();
		config = config.resolve(resolveOpts);

		// render
		ConfigRenderOptions renderOpts = ConfigRenderOptions.defaults()
			.setOriginComments(false)
			.setOriginComments(false);
		System.out.print(config.root().render(renderOpts));
	}
}
