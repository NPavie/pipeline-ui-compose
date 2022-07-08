/**
 * taken from the original org.daisy.pipeline.gui.databridge
 */
package org.daisy.pipeline.ui.bridge;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;

import org.daisy.common.xproc.XProcOptionInfo;
import org.daisy.common.xproc.XProcPipelineInfo;
import org.daisy.common.xproc.XProcPortInfo;
import org.daisy.pipeline.datatypes.DatatypeRegistry;
import org.daisy.pipeline.script.XProcOptionMetadata;
import org.daisy.pipeline.script.XProcPortMetadata;
import org.daisy.pipeline.script.XProcScript;

// representation of a pipeline script in a GUI-friendly way
public class Script {
        private String id;
        private String name;
        private String description;
        private ArrayList<ScriptField> requiredOptionFields;
        private ArrayList<ScriptField> optionalOptionFields;
        private XProcScript xprocScript;
        
        public Script(String id, XProcScript script, DatatypeRegistry datatypeRegistry) {
                requiredOptionFields = new ArrayList<ScriptField>();
                optionalOptionFields = new ArrayList<ScriptField>();
                xprocScript = script;
                
                this.id = id;
                name = script.getName();
                description = script.getDescription();
                
                XProcPipelineInfo scriptInfo = script.getXProcPipelineInfo();
                for (XProcPortInfo portInfo : scriptInfo.getInputPorts()) {
                        XProcPortMetadata metadata = script.getPortMetadata(portInfo.getName());
                        ScriptField field = new ScriptField(portInfo, metadata, ScriptField.FieldType.INPUT);
                        if (field.isRequired()) {
                                requiredOptionFields.add(field);
                        } else {
                                optionalOptionFields.add(field);
                        }
                }
                
                for (XProcOptionInfo optionInfo : scriptInfo.getOptions()) {
                        XProcOptionMetadata metadata = script.getOptionMetadata(optionInfo.getName());
                        ScriptField field = new ScriptField(optionInfo, metadata, datatypeRegistry);
                        if (field.isRequired()) {
                                requiredOptionFields.add(field);
                        }
                        else {
                                optionalOptionFields.add(field);
                        }
                                
                }
        }

        protected Script(
                String id,
                String name,
                String description,
                ArrayList<ScriptField> requiredOptionFields,
                ArrayList<ScriptField> optionalOptionFields
        ){
                this.requiredOptionFields = requiredOptionFields;
                this.optionalOptionFields = optionalOptionFields;
                this.id = id;
                this.name = name;
                this.description = description;
        }

        public static Script MockDaisy3ToEpub3(){
                return new Script(
                        "daisy3-to-epub3",
                        "DAISY 3 to EPUB 3",
                        "Transforms a DAISY 3 publication into an EPUB 3 publication",
                        new ArrayList<ScriptField>(){{
                                add(ScriptField.mockPort(
                                        "source",
                                        "OPF",
                                        "The package file of the input DTB.",
                                        false,
                                        "application/oebps-package+xml",
                                        ScriptField.FieldType.INPUT,
                                        true,
                                        true
                                ));
                                add(ScriptField.mockOption(
                                        "output-dir",
                                        "EPUB",
                                        "The produced EPUB.",
                                        true,
                                        "",
                                        ScriptField.DataType.DIRECTORY,
                                        true,
                                        true,
                                        XProcOptionMetadata.Output.RESULT,
                                        ""
                                ));
                                add(ScriptField.mockOption(
                                        "temp-dir",
                                        "Temporary directory",
                                        "Directory used for temporary files.",
                                        true,
                                        "",
                                        ScriptField.DataType.DIRECTORY,
                                        true,
                                        true,
                                        XProcOptionMetadata.Output.TEMP,
                                        ""
                                ));

                        }},
                        new ArrayList<ScriptField>(){{
                                add(ScriptField.mockOption(
                                        "mediaoverlays",
                                        "Include Media Overlays",
                                        "Whether or not to include media overlays and associated audio files\n" +
                                                "(true or false).",
                                        true,
                                        "",
                                        ScriptField.DataType.BOOLEAN,
                                        false,
                                        true,
                                        XProcOptionMetadata.Output.TEMP,
                                        "true"
                                ));
                                add(ScriptField.mockOption(
                                        "assert-valid",
                                        "Assert validity",
                                        "Whether to stop processing and raise an error on validation issues.",
                                        true,
                                        "",
                                        ScriptField.DataType.BOOLEAN,
                                        false,
                                        true,
                                        XProcOptionMetadata.Output.TEMP,
                                        "true"
                                ));
                                add(ScriptField.mockOption(
                                        "_:chunk-size",
                                        "Chunk size",
                                        "The maximum size of HTML files in kB. Specify \"-1\" for no maximum.\n" +
                                                "\n" +
                                                "Top-level sections in the DTBook become separate HTML files in the resulting EPUB, and are further\n" +
                                                "split up if they exceed the given maximum size.",
                                        true,
                                        "",
                                        ScriptField.DataType.INTEGER,
                                        false,
                                        true,
                                        XProcOptionMetadata.Output.TEMP,
                                        "-1"
                                ));
                        }}
                );
        }
        
        public String getId() {
                return id;
        }
        public String getName() {
                return name;
        }
        public String getDescription() {
                return description;
        }
        public Iterable<ScriptField> getRequiredOptionFields() {
                return requiredOptionFields;
        }
        public Iterable<ScriptField> getOptionalOptionFields() {
                return optionalOptionFields;
        }
        public XProcScript getXProcScript() {
                return xprocScript;
        }

        public static class ScriptComparator implements Comparator<Script> {

                @Override
                public int compare(Script o1, Script o2) {
                        return Collator.getInstance().compare(o1.getName(),o2.getName());
                }
        }
        
}
