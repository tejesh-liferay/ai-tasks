/**
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

const OpenAIImageModelNodeConfigureForm = ({ nodeParameters, onChange }) => {
  const [activeTab, setActiveTab] = useState(0);

  const qualityOptions = {
    standard: 'Standard',
    hd: 'HD',
  };

  const responseFormatOptions = {
    b64_json: 'Base64 encoded JSON',
    url: 'URL',
  };

  const styleOptions = {
    natural: 'Natural',
    vivid: 'Vivid',
  };

  const { t } = useTranslation();

  return (
    <div>
      <div class="alert alert-info">{t('openai-image-model-node-description')}</div>

      <Tabs onChange={setActiveTab}>
        <Tab id="generalSettings" label={t('general')} />
        <Tab id="modelSettings" label={t('model')} />
        <Tab id="promptSettings" label={t('prompt')} />
        <Tab id="outputSettings" label={t('output')} />
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

            <SelectField
              label="Response format"
              helpText="The response format."
              onChange={onChange}
              options={responseFormatOptions}
              parameterName="responseFormat"
              parameterValue={nodeParameters.responseFormat}
            />
            <SelectField
              label="Quality"
              helpText="See https://platform.openai.com/docs/api-reference/images/create"
              onChange={onChange}
              options={qualityOptions}
              parameterName="quality"
              parameterValue={nodeParameters.quality}
            />
            <SelectField
              helpText="See https://platform.openai.com/docs/api-reference/images/create"
              onChange={onChange}
              options={styleOptions}
              parameterName="style"
              parameterValue={nodeParameters.style}
            />

            <TextField
              helpText="See https://platform.openai.com/docs/api-reference/images/create for more information."
              onChange={onChange}
              parameterName="size"
              parameterValue={nodeParameters.size}
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
        <div id={'advancedSettings'}>
          <div>
            <TextField
              helpText="See https://platform.openai.com/docs/api-reference/images/create for more information."
              onChange={onChange}
              parameterName="user"
              parameterValue={nodeParameters.user}
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
export default OpenAIImageModelNodeConfigureForm;
