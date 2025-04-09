/**
 * @author Petteri Karttunen
 * @author Louis-Guillaume Durand
 */
import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';

import { TabContent } from '../../ui/TabContent';
import { Tab, Tabs } from '../../ui/Tabs';
import JsonField from './fields/JsonField';
import NumberField from './fields/NumberField';
import SelectField from './fields/SelectField';
import TextField from './fields/TextField';

const WebhookNodeConfigureForm = ({ nodeParameters, onChange }) => {
  const [activeTab, setActiveTab] = useState(0);

  const httpMethodOptions = {
    GET: 'GET',
    POST: 'POST',
    PUT: 'PUT',
    DELETE: 'DELETE',
    PATCH: 'PATCH',
  };

  const { t } = useTranslation();

  return (
    <div>
      <div class="alert alert-info">{t('webhook-node-description')}</div>

      <Tabs onChange={setActiveTab}>
        <Tab id="generalSettings" label={t('general')} />
        <Tab id="payloadSettings" label={t('payload')} />
        <Tab id="outputSettings" label={t('output')} />
      </Tabs>

      <TabContent activeTab={activeTab}>
        <div id="generalSettings">
          <div>
            <TextField
              helpText="Enter the webhook URL."
              onChange={onChange}
              parameterName="url"
              parameterValue={nodeParameters.url}
            />
          </div>
        </div>
        <div id="payloadSettings">
          <div>
            <SelectField
              onChange={onChange}
              options={httpMethodOptions}
              parameterName="httpMethod"
              parameterValue={nodeParameters.httpMethod}
            />

            <JsonField
              onChange={onChange}
              parameterName="requestBody"
              parameterValue={nodeParameters.requestBody}
            />

            <JsonField
              onChange={onChange}
              parameterName="requestHeaders"
              parameterValue={nodeParameters.requestHeaders}
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
      </TabContent>
    </div>
  );
};
export default WebhookNodeConfigureForm;
