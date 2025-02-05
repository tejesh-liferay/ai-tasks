import React, { useCallback, useEffect, useState } from 'react';
import { NavLink, useParams } from 'react-router-dom';
import { JsonEditor } from 'json-edit-react';

import LoadingIndicator from './ui/LoadingIndicator';
import Icon from './ui/Icon';
import Alert from './ui/Alert';
import Label from './ui/Label';
import { useAITasksContext } from '../contexts/AITasksContext';
import NavigationBar from './ui/NavigationBar';
import AITaskChatPreview from './AITaskChatPreview';
import { Tab, Tabs } from './ui/Tabs';
import AITaskFlowEditor from './flow/AITaskFlowEditor';
import { ROUTE_TASK_LIST } from '../constants/AITasksRoutesConstants';
import { DnDProvider } from '../contexts/DnDContext';

const AITaskEdit = () => {
  const { fetchTask, selectedTask, setSelectedTask, updateTask, loading, error } =
    useAITasksContext();
  const [isChatPreviewOpen, setIsChatPreviewOpen] = useState(false);
  const { id } = useParams();

  const fetchCurrentTask = useCallback(async () => {
    const task = await fetchTask(id);
    setSelectedTask(task);
  }, [id]);

  useEffect(() => {
    fetchCurrentTask();
  }, []);

  const handleConfigurationChange = async ({ newData }) => {
    const updatedTask = {
      ...selectedTask,
      configuration: { ...newData },
    };
    updateTask(updatedTask);
  };

  const handleFlowConfigurationChange = (newConfig) => {
    const updatedTask = {
      ...selectedTask,
      configuration: { ...newConfig },
    };
    updateTask(updatedTask);
  };

  const handleInputOnChange = (e, key) => {
    let value = e.target.value;
    if (key === 'externalReferenceCode') {
      value = value.trim().replaceAll(' ', '');
    }
    const updatedTask = {
      ...selectedTask,
      [key]: value,
    };

    setSelectedTask(updatedTask);
  };

  const handleBasicInfoChanges = async () => {
    const currentSavedTask = await fetchTask(selectedTask.id);
    if (
      currentSavedTask.title !== selectedTask.title ||
      currentSavedTask.externalReferenceCode !== selectedTask.externalReferenceCode
    ) {
      updateTask(selectedTask);
    }
  };

  if (loading) {
    return (
      <div className={'d-flex justify-content-center align-items-center vh-100'}>
        <LoadingIndicator />
      </div>
    );
  }

  if (error) {
    return <Alert type={'danger'} lead={'Error'} message={error} />;
  }

  if (!selectedTask) {
    return <div>Item not found</div>;
  }

  return (
    <>
      <NavigationBar>
        <NavLink to={ROUTE_TASK_LIST} slot="left" className={'btn btn-monospaced btn-sm'}>
          <Icon name={'angle-left'} />
        </NavLink>
        <div slot="left" className={'border-right ml-sm-2 mr-3 pr-3'}>
          <Label type={'info'} className={'ml-2 mt-2 mb-2'}>
            {'version ' + selectedTask.version}
          </Label>
          <input
            id={'title'}
            className={'h1 form-control form-control-inline px-2 py-0'}
            type={'text'}
            value={selectedTask.title}
            onChange={(e) => {
              handleInputOnChange(e, 'title');
            }}
            onBlur={(e) => {
              handleBasicInfoChanges();
            }}
            onKeyDown={(e) => {
              if (e.code === 'Enter') {
                handleBasicInfoChanges();
              }
            }}
          />
        </div>
        <div slot="center">
          <div>
            <span className="text-secondary">ID:</span>
            <strong className="ml-2">{selectedTask.id}</strong>
          </div>
          <div className="d-flex flex-row align-items-center">
            <label
              htmlFor={'erc'}
              className={'text-secondary pt-1'}
              style={{ fontWeight: 'normal' }}
            >
              ERC:
            </label>
            <input
              id={'erc'}
              className={'form-control form-control-inline px-2 py-0 ml-2'}
              type={'text'}
              value={selectedTask.externalReferenceCode}
              onChange={(e) => {
                handleInputOnChange(e, 'externalReferenceCode');
              }}
              onBlur={(e) => {
                handleBasicInfoChanges();
              }}
              onKeyDown={(e) => {
                if (e.code === 'Enter') {
                  handleBasicInfoChanges();
                }
              }}
            />
          </div>
        </div>
        <div slot="right">
          <button
            className={'btn btn-secondary'}
            onClick={(e) => {
              e.preventDefault();
              setIsChatPreviewOpen(!isChatPreviewOpen);
            }}
          >
            {(isChatPreviewOpen ? 'Close' : 'Open') + ' Chat Preview'}
          </button>
        </div>
      </NavigationBar>
      <div className={'sheet sheet-xl mt-6 px-0'}>
        <Tabs className={'justify-content-center'}>
          <Tab id={'flowEditor'} label={'Flow Editor'} className={'px-0 py-0'}>
            <div>
              <DnDProvider>
                <AITaskFlowEditor onConfigChange={handleFlowConfigurationChange} />
              </DnDProvider>
            </div>
          </Tab>
          <Tab id={'jsonEditor'} label={'JSON Editor'} className={'container'}>
            <div>
              <JsonEditor
                data={selectedTask.configuration}
                rootName={'configuration'}
                indent={2}
                collapse={2}
                collapseAnimationTime={150}
                maxWidth={'100%'}
                onEdit={handleConfigurationChange}
                onAdd={handleConfigurationChange}
                onDelete={handleConfigurationChange}
              />
            </div>
          </Tab>
        </Tabs>
      </div>
      <AITaskChatPreview isOpen={isChatPreviewOpen} setIsOpen={setIsChatPreviewOpen} />
    </>
  );
};

export default AITaskEdit;
