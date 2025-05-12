/**
 * @author Louis-Guillaume Durand
 * @author Petteri Karttunen
 */
import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';

import { TabContent } from '../../ui/TabContent';
import { Tab, Tabs } from '../../ui/Tabs';
import BooleanField from './fields/BooleanField';
import JsonField from './fields/JsonField';
import NumberField from './fields/NumberField';
import RangeField from './fields/RangeField';
import SelectField from './fields/SelectField';
import TextAreaField from './fields/TextAreaField';
import TextField from './fields/TextField';
import ToggleField from './fields/ToggleField';

const GeminiChatModelNodeConfigureForm = ({ nodeParameters, onChange }) => {
  const [activeTab, setActiveTab] = useState(0);

  const geminiModelOptions = {
    'gemini-2.0-flash-exp': 'Gemini 2.0 Flash (gemini-2.0-flash-exp)',
    'gemini-1.5-flash': 'Gemini 1.5 Flash (gemini-1.5-flash)',
    'gemini-1.5-flash-8b': 'Gemini 1.5 Flash-8B (gemini-1.5-flash-8b)',
    'gemini-1.5-pro': 'Gemini 1.5 Pro (gemini-1.5-pro)',
  };

  const locationOptions = {
    'us-central1': 'Iowa (us-central1)',
    'us-west1': 'Oregon (us-west1)',
    'us-west4': 'Las Vegas (us-west4)',
    'us-east4': 'N. Virginia (us-east4)',
    'europe-west1': 'Belgium (europe-west1)',
    'europe-north1': 'Finland (europe-north1)',
    'asia-southeast1': 'Singapore (asia-southeast1)',
  };

  const { t } = useTranslation();

  return (
    <div>
      <div class="alert alert-info">{t('gemini-chat-model-node-description')}</div>

      <Tabs onChange={setActiveTab}>
        <Tab id="generalSettings" label={t('general')} />
        <Tab id="modelSettings" label={t('model')} />
        <Tab id="promptSettings" label={t('prompt')} />
        <Tab id="toolsSettings" label={t('tools')} />
        <Tab id="outputSettings" label={t('output')} />
        <Tab id="advancedSettings" label={t('advanced')} />
      </Tabs>

      <TabContent activeTab={activeTab}>
        <div id="generalSettings">
          <div>
            <TextField
              onChange={onChange}
              parameterName="project"
              parameterValue={nodeParameters.project}
              required="true"
            />
            <SelectField
              onChange={onChange}
              options={locationOptions}
              parameterName="location"
              parameterValue={nodeParameters.location}
            />
            <BooleanField
              onChange={onChange}
              parameterName="useGoogleSearch"
              parameterValue={nodeParameters.useGoogleSearch}
            />
            <ToggleField
              onChange={onChange}
              parameterName="useCache"
              parameterValue={nodeParameters.useCache}
            />
            <ToggleField
              onChange={onChange}
              parameterName="useChatMemory"
              parameterValue={nodeParameters.useChatMemory}
            />
            <NumberField
              onChange={onChange}
              parameterName="memoryMaxMessages"
              parameterValue={nodeParameters.memoryMaxMessages}
            />
          </div>
        </div>

        <div id="modelSettings">
          <div>
            <SelectField
              helpText="The Gemini model variant name to use: https://ai.google.dev/gemini-api/docs/models/gemini"
              onChange={onChange}
              options={geminiModelOptions}
              parameterName="model"
              parameterValue={nodeParameters.model}
            />
            <RangeField
              onChange={onChange}
              parameterName="temperature"
              parameterValue={nodeParameters.temperature}
            />
            <NumberField
              min="1"
              onChange={onChange}
              parameterName="topK"
              parameterValue={nodeParameters.topK}
            />
            <NumberField
              min="0"
              onChange={onChange}
              parameterName="topP"
              parameterValue={nodeParameters.topP}
            />
            <NumberField
              min="1"
              onChange={onChange}
              parameterName="maxOutputTokens"
              parameterValue={nodeParameters.maxOutputTokens}
            />
          </div>
        </div>
        <div id="promptSettings">
          <div>
            <TextAreaField
              defaultValue="{{input.text}}"
              onChange={onChange}
              parameterName="promptTemplate"
              parameterValue={nodeParameters.promptTemplate}
            />
            <TextAreaField
              onChange={onChange}
              parameterName="systemMessage"
              parameterValue={nodeParameters.systemMessage}
            />
          </div>
        </div>
        <div id="toolsSettings">
          <div>
            <JsonField
              onChange={onChange}
              parameterName="tools"
              parameterValue={nodeParameters.tools}
            />
            <JsonField
              onChange={onChange}
              parameterName="toolProvider"
              parameterValue={nodeParameters.toolProvider}
            />
          </div>
        </div>
        <div id="outputSettings">
          <div>
            <TextField
              onChange={onChange}
              parameterName="outputParameterName"
              parameterValue={nodeParameters.outputParameterName}
            />
            <TextField
              onChange={onChange}
              parameterName="taskContextOutputParameterName"
              parameterValue={nodeParameters.taskContextOutputParameterName}
            />
          </div>
        </div>

        <div id="advancedSettings">
          <div>
            <JsonField
              onChange={onChange}
              parameterName="responseSchema"
              parameterValue={nodeParameters.responseSchema}
            />

            <JsonField
              onChange={onChange}
              parameterName="safetySettings"
              parameterValue={nodeParameters.safetySettings}
            />

            <TextField
              onChange={onChange}
              parameterName="allowedFunctionNames"
              parameterValue={nodeParameters.allowedFunctionNames}
            />

            <TextField
              onChange={onChange}
              parameterName="vertexSearchDatastore"
              parameterValue={nodeParameters.vertexSearchDatastore}
            />
            <NumberField
              onChange={onChange}
              parameterName="maxRetries"
              parameterValue={nodeParameters.maxRetries}
            />
            <BooleanField
              onChange={onChange}
              parameterName="logRequests"
              parameterValue={nodeParameters.logRequests}
            />
            <BooleanField
              onChange={onChange}
              parameterName="logResponses"
              parameterValue={nodeParameters.logResponses}
            />
          </div>
        </div>
      </TabContent>
    </div>
  );
};

export default GeminiChatModelNodeConfigureForm;
