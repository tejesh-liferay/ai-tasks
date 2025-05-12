/**
 * @author Petteri Karttunen
 * @author Louis-Guillaume Durand
 */
import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';

import { TabContent } from '../../ui/TabContent';
import { Tab, Tabs } from '../../ui/Tabs';
import BooleanField from './fields/BooleanField';
import JsonField from './fields/JsonField';
import NumberField from './fields/NumberField';
import RangeField from './fields/RangeField';
import TextAreaField from './fields/TextAreaField';
import TextField from './fields/TextField';
import ToggleField from './fields/ToggleField';

const OpenAIStreamingChatModelNodeConfigureForm = ({ nodeParameters, onChange }) => {
  const [activeTab, setActiveTab] = useState(0);

  const { t } = useTranslation();

  return (
    <div>
      <div class="alert alert-info">{t('openai-streaming-chat-model-node-description')}</div>

      <Tabs onChange={setActiveTab}>
        <Tab id="generalSettings" label={t('general')} />
        <Tab id="modelSettings" label={t('model')} />
        <Tab id="promptSettings" label={t('prompt')} />
        <Tab id="toolsSettings" label={t('tools')} />
        <Tab id="advancedSettings" label={t('advanced')} />
      </Tabs>

      <TabContent activeTab={activeTab}>
        <div id="generalSettings">
          <div>
            <TextField
              onChange={onChange}
              parameterName="apiKey"
              parameterValue={nodeParameters.apiKey}
              required="true"
            />
            <TextField
              onChange={onChange}
              parameterName="baseUrl"
              parameterValue={nodeParameters.baseUrl}
              required="true"
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
            <TextField
              onChange={onChange}
              parameterName="modelName"
              parameterValue={nodeParameters.modelName}
              required="true"
            />
            <RangeField
              onChange={onChange}
              parameterName="temperature"
              parameterValue={nodeParameters.temperature}
            />
            <NumberField
              helpText="See https://platform.openai.com/docs/api-reference/chat for more information."
              min="1"
              onChange={onChange}
              parameterName="frequencyPenalty"
              parameterValue={nodeParameters.repeatPenalty}
            />
            <NumberField
              helpText="See https://platform.openai.com/docs/api-reference/chat for more information."
              min="1"
              onChange={onChange}
              parameterName="presencePenalty"
              parameterValue={nodeParameters.repeatPenalty}
            />

            <NumberField
              onChange={onChange}
              parameterName="seed"
              parameterValue={nodeParameters.seed}
            />
            <TextField
              onChange={onChange}
              parameterName="stop"
              parameterValue={nodeParameters.stop}
              required="true"
            />

            <NumberField
              min="0"
              onChange={onChange}
              parameterName="topP"
              parameterValue={nodeParameters.topP}
            />
            <NumberField
              onChange={onChange}
              parameterName="maxTokens"
              parameterValue={nodeParameters.maxTokens}
            />
            <JsonField
              helpText="See https://platform.openai.com/docs/api-reference/chat for more information."
              onChange={onChange}
              parameterName="logitBias"
              parameterValue={nodeParameters.logitBias}
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
        <div id={'advancedSettings'}>
          <div>
            <JsonField
              helpText="See https://platform.openai.com/docs/api-reference/chat for more information."
              onChange={onChange}
              parameterName="customHeaders"
              parameterValue={nodeParameters.customHeaders}
            />

            <TextField
              helpText="See https://platform.openai.com/docs/api-reference/chat for more information."
              onChange={onChange}
              parameterName="user"
              parameterValue={nodeParameters.user}
            />
            <TextField
              helpText="See https://platform.openai.com/docs/api-reference/chat for more information."
              onChange={onChange}
              parameterName="organizationId"
              parameterValue={nodeParameters.organizationId}
            />

            <TextField
              onChange={onChange}
              parameterName="responseFormat"
              parameterValue={nodeParameters.responseFormat}
            />
            <NumberField
              onChange={onChange}
              parameterName="timeout"
              parameterValue={nodeParameters.timeout}
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

export default OpenAIStreamingChatModelNodeConfigureForm;
