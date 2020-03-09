package com.github.okapies.hoc2js;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import com.typesafe.config.*;

/**
 * Very simple utility for converting from Typesafe's HOCON to JSON.
 * See https://github.com/typesafehub/config/blob/master/HOCON.md
 */
public class Hoc2Js {
    public static void main(String[] args) {
        Config config = ConfigFactory.load();

        Config obj = ConfigFactory.empty();

        // parse
        ConfigParseOptions parseOpts = loadParseOpts(config);
        if (args.length > 0) {
            for (String filename : args) {
                obj = obj.withFallback(ConfigFactory.parseFile(new File(filename), parseOpts));
            }
        } else {
            // read from stdin instead of .conf files
            obj = ConfigFactory.parseReader(
                new BufferedReader(new InputStreamReader(System.in)), parseOpts);
        }

        // resolve
        ConfigResolveOptions resolveOpts = loadResolveOpts(config);
        obj = obj.resolve(resolveOpts.setAllowUnresolved(true));
        obj = obj.resolveWith(config, resolveOpts);

        // render
        ConfigRenderOptions renderOpts = loadRenderOpts(config);
        System.out.print(obj.root().render(renderOpts));
    }

    private static ConfigParseOptions loadParseOpts(Config config) {
        ConfigSyntax syntax = getNullableConfigSyntax(config, "hoc2js.parse.syntax");
        String originDescription = getNullableString(config, "hoc2js.parse.originDescription");
        boolean allowMissing = config.getBoolean("hoc2js.parse.allowMissing");

        return ConfigParseOptions.defaults()
            .setSyntax(syntax)
            .setOriginDescription(originDescription)
            .setAllowMissing(allowMissing);
    }

    private static ConfigResolveOptions loadResolveOpts(Config config) {
        boolean useSystemEnvironment = config.getBoolean("hoc2js.resolve.useSystemEnvironment");
        boolean allowUnresolved = config.getBoolean("hoc2js.resolve.allowUnresolved");

        return ConfigResolveOptions.defaults()
                .setUseSystemEnvironment(useSystemEnvironment)
                .setAllowUnresolved(allowUnresolved);
    }

    private static ConfigRenderOptions loadRenderOpts(Config config) {
        boolean originComments = config.getBoolean("hoc2js.render.originComments");
        boolean comments = config.getBoolean("hoc2js.render.comments");
        boolean formatted = config.getBoolean("hoc2js.render.formatted");
        boolean json = config.getBoolean("hoc2js.render.json");

        return ConfigRenderOptions.defaults()
                .setOriginComments(originComments)
                .setComments(comments)
                .setFormatted(formatted)
                .setJson(json);
    }

    private static String getNullableString(Config config, String path) {
        String ret;

        try {
            ret = config.getString(path);
        } catch (ConfigException.Missing e) {
            ret = null;
        }

        return ret;
    }

    private static ConfigSyntax getNullableConfigSyntax(Config config, String path) {
        ConfigSyntax ret;

        try {
            ret = ConfigSyntax.valueOf(config.getString(path).toUpperCase());
        } catch (ConfigException.Missing e) {
            ret = null;
        }

        return ret;
    }
}
