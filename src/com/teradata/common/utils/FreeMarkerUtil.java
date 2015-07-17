package com.teradata.common.utils;

import java.io.IOException;
import java.io.Writer;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreeMarkerUtil {
	private static Configuration cfg = null;
	static {
		cfg = new Configuration();
		cfg.setClassForTemplateLoading(FreeMarkerUtil.class, "/template/");
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		cfg.setNumberFormat("####");
	}

	private static Template getTemplate(String templateName) {
		try {
			return cfg.getTemplate(templateName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void genXML(String templateName, Object data, Writer out) {
		try {
			Template t = getTemplate(templateName);
			t.createProcessingEnvironment(data, out);
			t.process(data, out);
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
