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

const GoogleImagenNodeConfigureForm = ({ nodeParameters, onChange }) => {
  const [activeTab, setActiveTab] = useState(0);

  const locationOptions = {
    'us-central1': 'Iowa (us-central1)',
    'us-west1': 'Oregon (us-west1)',
    'us-west4': 'Las Vegas (us-west4)',
    'us-east4': 'N. Virginia (us-east4)',
    'europe-west1': 'Belgium (europe-west1)',
    'europe-north1': 'Finland (europe-north1)',
    'asia-southeast1': 'Singapore (asia-southeast1)',
  };

  const imageStyleOptions = {
    cyberpunk: 'Cyberpunk',
    digitalArt: 'Digital Art',
    landscape: 'Landscape',
    photograph: 'Photograph',
    popArt: 'Pop Art',
    sketch: 'Sketch',
    watercolor: 'Watercolor',
  };

  const { t } = useTranslation();

  return (
    <div>
      <div class="alert alert-info">{t('google-imagen-node-description')}</div>

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
              parameterName="endpoint"
              parameterValue={nodeParameters.endpoint}
              required="true"
            />
            <TextField
              onChange={onChange}
              parameterName="project"
              parameterValue={nodeParameters.project}
              required="true"
            />
            <TextField
              onChange={onChange}
              parameterName="publisher"
              parameterValue={nodeParameters.publisher}
              required="true"
            />
            <SelectField
              onChange={onChange}
              options={locationOptions}
              parameterName="location"
              parameterValue={nodeParameters.location}
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
            <NumberField
              helpText="Enter guidance scale (0-100)."
              min="0"
              onChange={onChange}
              parameterName="guidanceScale"
              parameterValue={nodeParameters.guidanceScale}
            />
            <TextField
              onChange={onChange}
              parameterName="sampleImageSize"
              parameterValue={nodeParameters.sampleImageSize}
              required="true"
            />
            <NumberField
              onChange={onChange}
              parameterName="seed"
              parameterValue={nodeParameters.seed}
            />
            <SelectField
              onChange={onChange}
              options={imageStyleOptions}
              parameterName="sampleImageStyle"
              parameterValue={nodeParameters.sampleImageStyle}
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
              parameterName="negativePrompt"
              parameterValue={nodeParameters.negativePrompt}
            />
            <TextField
              helpText="Enter 2 character language code. See supported languages at https://cloud.google.com/vertex-ai/generative-ai/docs/image/model-versioning."
              onChange={onChange}
              parameterName="language"
              parameterValue={nodeParameters.language}
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

export default GoogleImagenNodeConfigureForm;
