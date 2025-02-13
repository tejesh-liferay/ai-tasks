/**
 * @author Petteri Karttunen
 */

import React from 'react';

import { Tab, Tabs } from '../../ui/Tabs';

const GeminiChatModelNodeConfigureForm = ({ nodeParameters, onChange }) => {
    const rangeWidth = (nodeParameters.temperature / 2) * 100;
    return (
        <Tabs>
            <Tab id={'generalSettings'} label={'General Settings'}>
                <div>
                    <div className="form-group">
                        <label htmlFor="url">URL</label>
                        <input
                            type="text"
                            className="form-control"
                            id="url"
                            placeholder="Enter the URL"
                            value={nodeParameters.url}
                            onChange={(e) => {
                                onChange('url', e.currentTarget.value);
                            }}
                        />
                        <small className="form-text text-muted">
                            Webhook URL.
                        </small>
                    </div>
                    <div className="form-group">
                        <label htmlFor="method">HTTP Method</label>
                        <select
                            className="form-control"
                            id="method"
                            value={nodeParameters.method}
                            onChange={(e) => {
                                onChange('method', e.currentTarget.value);
                            }}
                        >
                            <option value={'GET'}>GET</option>
                            <option value={'POST'}>POST</option>
                            <option value={'PUT'}>PUT</option>
                            <option value={'DELETE'}>DELETE</option>
                            <option value={'PATCH'}>PATCH</option>
                        </select>
                        <small className="form-text text-muted">
                            HTTP method to use.
                        </small>
                    </div>
                </div>
            </Tab>
            <Tab id={'payloadSettings'} label={'Payload'}>
                <div>
                    <div className="form-group">
                        <label htmlFor="body">Body JSON</label>
                        <textarea
                            className="form-control"
                            id="body"
                            rows="3"
                            onChange={(e) => {
                                onChange('body', JSON.parse(e.currentTarget.value));
                            }}
                            value={JSON.stringify(nodeParameters.body, undefined, 2)}
                        />
                        <small className="form-text text-muted">
                            Enter the body payload as JSON.
                        </small>
                    </div>
                    <div className="form-group">
                        <label htmlFor="headers">Headers JSON</label>
                        <textarea
                            className="form-control"
                            id="headers"
                            rows="3"
                            onChange={(e) => {
                                onChange('headers', JSON.parse(e.currentTarget.value));
                            }}
                            value={JSON.stringify(nodeParameters.headers, undefined, 2)}
                        />
                        <small className="form-text text-muted">
                            Enter the headers as JSON.
                        </small>
                    </div>
                </div>
            </Tab>
            <Tab id={'outputSettings'} label={'Output'}>
                <div>
                    <div className="form-group">
                        <label htmlFor="url">Output parameter name</label>
                        <input
                            type="text"
                            className="form-control"
                            id="outputParameterName"
                            placeholder="Enter the name"
                            value={nodeParameters.outputParameterName}
                            onChange={(e) => {
                                onChange('outputParameterName', e.currentTarget.value);
                            }}
                        />
                        <small className="form-text text-muted">
                            The name of the output parameter.
                        </small>
                    </div>
                    <div className="form-group">
                        <label htmlFor="taskContextOutputParameterName">Task context output parameter name</label>
                        <input
                            type="text"
                            className="form-control"
                            id="taskContextOutputParameterName"
                            placeholder="Enter the name"
                            value={nodeParameters.taskContextOutputParameterName}
                            onChange={(e) => {
                                onChange('taskContextOutputParameterName', e.currentTarget.value);
                            }}
                        />
                        <small className="form-text text-muted">
                            The name of the task context output parameter. Use this if this node is not the final output node, but sharing output data with the next nodes in chain.
                        </small>
                    </div>
                </div>
            </Tab>
        </Tabs>
    );
};

export default GeminiChatModelNodeConfigureForm;
