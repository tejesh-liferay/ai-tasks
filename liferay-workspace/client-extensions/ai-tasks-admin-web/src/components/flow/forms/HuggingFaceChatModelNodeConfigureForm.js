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

const HuggingFaceChatModelNodeConfigureForm = ({ nodeParameters, onChange }) => {
  const [activeTab, setActiveTab] = useState(0);

  const { t } = useTranslation();

  return (
    <div>
      <div class="alert alert-info">{t('hugging-face-chat-model-node-description')}</div>

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
              parameterName="accessToken"
              parameterValue={nodeParameters.accessToken}
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
            <TextField
              onChange={onChange}
              parameterName="modelId"
              parameterValue={nodeParameters.modelId}
              required="true"
            />
            <RangeField
              onChange={onChange}
              parameterName="temperature"
              parameterValue={nodeParameters.temperature}
            />
            <NumberField
              min="1"
              onChange={onChange}
              parameterName="maxNewTokens"
              parameterValue={nodeParameters.maxNewTokens}
            />
            <BooleanField
              onChange={onChange}
              parameterName="waitForModel"
              parameterValue={nodeParameters.waitForModel}
            />
            <BooleanField
              onChange={onChange}
              parameterName="returnFullText"
              parameterValue={nodeParameters.returnFullText}
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
            <NumberField
              onChange={onChange}
              parameterName="timeout"
              parameterValue={nodeParameters.timeout}
            />
          </div>
        </div>
      </TabContent>
    </div>
  );
};

export default HuggingFaceChatModelNodeConfigureForm;
